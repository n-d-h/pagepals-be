package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.booking.ListBookingDto;
import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.dtos.transaction.ListTransactionDto;
import com.pagepal.capstone.dtos.transaction.TransactionDto;
import com.pagepal.capstone.dtos.transaction.TransactionFilterDto;
import com.pagepal.capstone.entities.postgre.Booking;
import com.pagepal.capstone.entities.postgre.Transaction;
import com.pagepal.capstone.enums.TransactionTypeEnum;
import com.pagepal.capstone.mappers.BookingMapper;
import com.pagepal.capstone.mappers.TransactionMapper;
import com.pagepal.capstone.repositories.TransactionRepository;
import com.pagepal.capstone.services.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Transactional
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final List<TransactionTypeEnum> transactionTypesForCustomer = Arrays
            .asList(
                    TransactionTypeEnum.BOOKING_PAYMENT,
                    TransactionTypeEnum.BOOKING_REFUND,
                    TransactionTypeEnum.DEPOSIT_TOKEN
            );
    private final List<TransactionTypeEnum> transactionTypesForReader = Arrays
            .asList(
                    TransactionTypeEnum.WITHDRAW_MONEY,
                    TransactionTypeEnum.BOOKING_DONE_RECEIVE
            );

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public ListTransactionDto getListTransactionForCustomer(UUID customerId, TransactionFilterDto filter) throws ParseException {

        if (filter.getPage() == null || filter.getPage() < 0)
            filter.setPage(0);
        if (filter.getPageSize() == null || filter.getPageSize() < 0)
            filter.setPageSize(10);

        Pageable pageable;
        pageable = PageRequest.of(filter.getPage(), filter.getPageSize(), Sort.by("createAt").descending());

        Page<Transaction> transactions;

        if (filter.getStartDate().isEmpty() && filter.getEndDate().isEmpty()) {
            if (filter.getTransactionType().isEmpty()) {
                transactions = transactionRepository
                        .findByCustomerIdAndTransactionTypes(
                                customerId,
                                transactionTypesForCustomer,
                                pageable
                        );
            } else {
                transactions = transactionRepository
                        .findByCustomerIdAndTransactionType(
                                customerId,
                                TransactionTypeEnum.valueOf(filter.getTransactionType()),
                                pageable
                        );
            }
        } else {
            Date startDate = dateFormat.parse(filter.getStartDate());
            Date endDate = dateFormat.parse(filter.getEndDate());
            if (filter.getTransactionType().isEmpty()) {
                transactions = transactionRepository
                        .findByCreateAtBetweenAndCustomerIdAndTransactionTypes(
                                startDate,
                                endDate,
                                customerId,
                                transactionTypesForCustomer,
                                pageable
                        );
            } else {
                transactions = transactionRepository
                        .findByCreateAtBetweenAndCustomerIdAndTransactionType(
                                startDate,
                                endDate,
                                customerId,
                                TransactionTypeEnum.valueOf(filter.getTransactionType()),
                                pageable
                        );
            }
        }

        ListTransactionDto list = new ListTransactionDto();
        if (transactions == null) {
            list.setList(Collections.emptyList());
            list.setPaging(null);
        } else {
            PagingDto pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(transactions.getTotalPages());
            pagingDto.setTotalOfElements(transactions.getTotalElements());
            pagingDto.setSort(transactions.getSort().toString());
            pagingDto.setCurrentPage(transactions.getNumber());
            pagingDto.setPageSize(transactions.getSize());

            list.setList(transactions.map(TransactionMapper.INSTANCE::toDto).toList());
            list.setPaging(pagingDto);
        }
        return list;

    }

    @Override
    public ListTransactionDto getListTransactionForReader(UUID readerId, TransactionFilterDto filter) throws ParseException {

        if (filter.getPage() == null || filter.getPage() < 0)
            filter.setPage(0);
        if (filter.getPageSize() == null || filter.getPageSize() < 0)
            filter.setPageSize(10);

        Pageable pageable;
        pageable = PageRequest.of(filter.getPage(), filter.getPageSize(), Sort.by("createAt").descending());

        Page<Transaction> transactions;

        if (filter.getStartDate().isEmpty() && filter.getEndDate().isEmpty()) {
            if (filter.getTransactionType().isEmpty()) {
                transactions = transactionRepository
                        .findByReaderIdAndTransactionTypes(
                                readerId,
                                transactionTypesForReader,
                                pageable
                        );
            } else {
                transactions = transactionRepository
                        .findByReaderIdAndTransactionType(
                                readerId,
                                TransactionTypeEnum.valueOf(filter.getTransactionType()),
                                pageable
                        );
            }
        } else {
            Date startDate = dateFormat.parse(filter.getStartDate());
            Date endDate = dateFormat.parse(filter.getEndDate());
            if (filter.getTransactionType().isEmpty()) {
                transactions = transactionRepository
                        .findByCreateAtBetweenAndReaderIdAndTransactionTypes(
                                startDate,
                                endDate,
                                readerId,
                                transactionTypesForReader,
                                pageable
                        );
            } else {
                transactions = transactionRepository
                        .findByCreateAtBetweenAndReaderIdAndTransactionType(
                                startDate,
                                endDate,
                                readerId,
                                TransactionTypeEnum.valueOf(filter.getTransactionType()),
                                pageable
                        );
            }
        }

        ListTransactionDto list = new ListTransactionDto();
        if (transactions == null) {
            list.setList(Collections.emptyList());
            list.setPaging(null);
        } else {
            PagingDto pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(transactions.getTotalPages());
            pagingDto.setTotalOfElements(transactions.getTotalElements());
            pagingDto.setSort(transactions.getSort().toString());
            pagingDto.setCurrentPage(transactions.getNumber());
            pagingDto.setPageSize(transactions.getSize());

            list.setList(transactions.map(TransactionMapper.INSTANCE::toDto).toList());
            list.setPaging(pagingDto);
        }
        return list;

    }

    @Override
    public TransactionDto getTransactionById(UUID id) {
        return TransactionMapper.INSTANCE.toDto(transactionRepository.findById(id).orElse(null));
    }
}
