package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.dtos.withdrawRequest.ListWithdrawRequestDto;
import com.pagepal.capstone.dtos.withdrawRequest.WithdrawQuery;
import com.pagepal.capstone.dtos.withdrawRequest.WithdrawRequestCreateDto;
import com.pagepal.capstone.dtos.withdrawRequest.WithdrawRequestReadDto;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.CurrencyEnum;
import com.pagepal.capstone.enums.TransactionStatusEnum;
import com.pagepal.capstone.enums.TransactionTypeEnum;
import com.pagepal.capstone.enums.WithdrawRequestStateEnum;
import com.pagepal.capstone.mappers.WithdrawRequestMapper;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.WithdrawRequestService;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class WithdrawRequestServiceImpl implements WithdrawRequestService {

    private final WithdrawRequestRepository withdrawRequestRepository;
    private final ReaderRepository readerRepository;
    private final WalletRepository walletRepository;
    private final DateUtils dateUltils;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    public List<WithdrawRequestReadDto> getAllWithdrawRequests() {
        List<WithdrawRequest> withdrawRequests = withdrawRequestRepository.findByState(WithdrawRequestStateEnum.PENDING);
        return withdrawRequests.stream().map(WithdrawRequestMapper.INSTANCE::toDto).toList();
    }

    @Override
    public ListWithdrawRequestDto getWithdrawRequestsByReaderId(UUID readerId, WithdrawQuery withdrawQuery) {
        if (withdrawQuery.getPage() == null || withdrawQuery.getPage() < 0)
            withdrawQuery.setPage(0);
        if (withdrawQuery.getPageSize() == null || withdrawQuery.getPageSize() < 0)
            withdrawQuery.setPageSize(10);

        Pageable pageable;
        if (withdrawQuery.getSort() != null && withdrawQuery.getSort().equals("desc")) {
            pageable = PageRequest.of(withdrawQuery.getPage(), withdrawQuery.getPageSize(), Sort.by("createdAt").descending());
        } else {
            pageable = PageRequest.of(withdrawQuery.getPage(), withdrawQuery.getPageSize(), Sort.by("createdAt").ascending());
        }

        Page<WithdrawRequest> withdrawRequests = withdrawRequestRepository.findByReaderId(readerId, pageable);

        ListWithdrawRequestDto list = new ListWithdrawRequestDto();
        if (withdrawRequests == null) {
            list.setList(Collections.emptyList());
            list.setPaging(null);
            return list;
        } else {
            PagingDto pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(withdrawRequests.getTotalPages());
            pagingDto.setTotalOfElements(withdrawRequests.getTotalElements());
            pagingDto.setSort(withdrawRequests.getSort().toString());
            pagingDto.setCurrentPage(withdrawRequests.getNumber());
            pagingDto.setPageSize(withdrawRequests.getSize());

            list.setList(withdrawRequests.map(WithdrawRequestMapper.INSTANCE::toDto).toList());
            list.setPaging(pagingDto);
            return list;
        }
    }

    @Override
    public WithdrawRequestReadDto getWithdrawRequestById(UUID id) {
        return WithdrawRequestMapper.INSTANCE
                .toDto(withdrawRequestRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Withdraw request not found"))
                );
    }

    @Override
    public WithdrawRequestReadDto createWithdrawRequest(UUID readerId, WithdrawRequestCreateDto withdrawRequestCreateDto) {
        Reader reader = readerRepository.findById(readerId).orElseThrow(() -> new EntityNotFoundException("Reader not found"));

        Wallet wallet = reader.getAccount().getWallet();
        if (wallet.getCash() < withdrawRequestCreateDto.getAmount()) {
            throw new ValidationException("Insufficient balance");
        }

        wallet.setCash(wallet.getCash() - withdrawRequestCreateDto.getAmount());
        wallet = walletRepository.save(wallet);
        
        WithdrawRequest withdrawRequest = new WithdrawRequest();
        Transaction transaction = new Transaction();

        if (wallet != null) {

            transaction.setAmount((double) withdrawRequestCreateDto.getAmount());
            transaction.setWallet(wallet);
            transaction.setTransactionType(TransactionTypeEnum.WITHDRAW_MONEY);
            transaction.setCreateAt(dateUltils.getCurrentVietnamDate());
            transaction.setCurrency(CurrencyEnum.DOLLAR);
            transaction.setStatus(TransactionStatusEnum.SUCCESS);
            transaction = transactionRepository.save(transaction);

            withdrawRequest.setReader(reader);
            withdrawRequest.setAmount(withdrawRequestCreateDto.getAmount());
            withdrawRequest.setBankName(withdrawRequestCreateDto.getBankName());
            withdrawRequest.setBankAccountNumber(withdrawRequestCreateDto.getBankAccountNumber());
            withdrawRequest.setBankAccountName(withdrawRequestCreateDto.getBankAccountName());
            withdrawRequest.setCreatedAt(dateUltils.getCurrentVietnamDate());
            withdrawRequest.setUpdatedAt(dateUltils.getCurrentVietnamDate());
            withdrawRequest.setState(WithdrawRequestStateEnum.PENDING);
            withdrawRequest = withdrawRequestRepository.save(withdrawRequest);
        }
        
        if(withdrawRequest == null || transaction == null) {
            throw new RuntimeException("Failed to create withdraw request");
        }

        return WithdrawRequestMapper.INSTANCE.toDto(withdrawRequest);
    }

    @Override
    public WithdrawRequestReadDto acceptWithdrawRequest(UUID id, String imgUrl, UUID staffId) {
        WithdrawRequest withdrawRequest = withdrawRequestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Withdraw request not found"));

        if(withdrawRequest.getState() != WithdrawRequestStateEnum.PENDING) {
            throw new RuntimeException("Withdraw request is processing or rejected");
        }

        Account accountStaff = accountRepository.findById(staffId).orElseThrow(() -> new EntityNotFoundException("Staff not found"));
        withdrawRequest.setState(WithdrawRequestStateEnum.ACCEPTED);
        withdrawRequest.setTransactionImage(imgUrl);
        withdrawRequest.setStaffId(staffId);
        withdrawRequest.setStaffName(accountStaff.getFullName());
        withdrawRequest.setUpdatedAt(dateUltils.getCurrentVietnamDate());
        withdrawRequest.setState(WithdrawRequestStateEnum.ACCEPTED);
        withdrawRequest = withdrawRequestRepository.save(withdrawRequest);

        if (withdrawRequest == null) {
            throw new RuntimeException("Failed to accept withdraw request");
        }

        return WithdrawRequestMapper.INSTANCE.toDto(withdrawRequest);
    }

    @Override
    public WithdrawRequestReadDto rejectWithdrawRequest(UUID id, String reason, UUID staffId) {
        WithdrawRequest withdrawRequest = withdrawRequestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Withdraw request not found"));

        if(withdrawRequest.getState() != WithdrawRequestStateEnum.PENDING) {
            throw new RuntimeException("Withdraw request is processing or rejected");
        }

        Account accountStaff = accountRepository.findById(staffId).orElseThrow(() -> new EntityNotFoundException("Staff not found"));
        withdrawRequest.setState(WithdrawRequestStateEnum.ACCEPTED);
        withdrawRequest.setRejectReason(reason);
        withdrawRequest.setStaffId(staffId);
        withdrawRequest.setStaffName(accountStaff.getFullName());
        withdrawRequest.setUpdatedAt(dateUltils.getCurrentVietnamDate());
        withdrawRequest.setState(WithdrawRequestStateEnum.REJECTED);
        withdrawRequest = withdrawRequestRepository.save(withdrawRequest);

        if (withdrawRequest == null) {
            throw new RuntimeException("Failed to reject withdraw request");
        }

        Wallet wallet = withdrawRequest.getReader().getAccount().getWallet();
        wallet.setCash(wallet.getCash() + withdrawRequest.getAmount());
        wallet = walletRepository.save(wallet);

        Transaction transaction = new Transaction();

        if(wallet != null) {
            transaction.setAmount((double) withdrawRequest.getAmount());
            transaction.setWallet(wallet);
            transaction.setTransactionType(TransactionTypeEnum.WITHDRAW_REFUND);
            transaction.setCreateAt(dateUltils.getCurrentVietnamDate());
            transaction.setCurrency(CurrencyEnum.DOLLAR);
            transaction.setStatus(TransactionStatusEnum.SUCCESS);
            transaction = transactionRepository.save(transaction);
        }

        if(transaction == null || wallet == null) {
            throw new RuntimeException("Failed to refund withdraw request");
        }

        return WithdrawRequestMapper.INSTANCE.toDto(withdrawRequest);
    }
}
