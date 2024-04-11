package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.request.RequestDto;
import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.AccountState;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.Request;
import com.pagepal.capstone.enums.RequestStateEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.RequestMapper;
import com.pagepal.capstone.repositories.AccountRepository;
import com.pagepal.capstone.repositories.AccountStateRepository;
import com.pagepal.capstone.repositories.ReaderRepository;
import com.pagepal.capstone.repositories.RequestRepository;
import com.pagepal.capstone.services.EmailService;
import com.pagepal.capstone.services.RequestService;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final AccountRepository accountRepository;

    private final ReaderRepository readerRepository;

    private final AccountStateRepository accountStateRepository;

    private final EmailService emailService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final String READER_STATE_ACTIVE = "READER_ACTIVE";

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final DateUtils dateUtils;

    @Secured("STAFF")
    @Override
    public List<RequestDto> getListRequest() {
        List<Request> requests = requestRepository.findAll();
        return requests.stream().map(RequestMapper.INSTANCE::toDto).toList();
    }

    @Secured("STAFF")
    @Override
    public RequestDto getRequestById(UUID requestId) {
        return RequestMapper.INSTANCE.toDto(requestRepository.findById(requestId).orElseThrow(
                () -> new EntityNotFoundException("Request not found")
        ));
    }

    @Secured("STAFF")
    @Override
    public RequestDto updateRequestInterview(UUID staffId, UUID requestId, String interviewAt, String description) throws ParseException {
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new EntityNotFoundException("Request not found")
        );
        Account account = accountRepository.findById(staffId).orElseThrow(
                () -> new EntityNotFoundException("Staff not found")
        );

        Date startDate = dateFormat.parse(interviewAt);

        String roomId = generateRoomId();

        request.setMeetingCode(roomId);
        request.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        request.setInterviewAt(startDate);
        request.setStaffId(account.getId());
        request.setStaffName(account.getFullName());
        request.setDescription(description);
        request.setState(RequestStateEnum.INTERVIEW_PENDING);

        request = requestRepository.save(request);

        if (request != null) {
            Reader reader = request.getReader();
            String email = reader.getAccount().getEmail();
            if (email != null && !email.isEmpty()) {
                emailService.sendSimpleEmail(email,
                        "Interview schedule",
                        "You have an interview schedule at: " + startDate + "; Please check your request on our website.");
            }
            return RequestMapper.INSTANCE.toDto(request);
        }
        return null;
    }

    @Secured("STAFF")
    @Override
    public RequestDto rejectRequest(UUID staffId, UUID requestId, String description) {
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new EntityNotFoundException("Request not found")
        );
        Account account = accountRepository.findById(staffId).orElseThrow(
                () -> new EntityNotFoundException("Staff not found")
        );

        request.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        request.setStaffId(account.getId());
        request.setStaffName(account.getFullName());
        request.setDescription(description);
        request.setState(RequestStateEnum.REJECT);
        request = requestRepository.save(request);

        if (request != null) {
            Reader reader = request.getReader();
            String email = reader.getAccount().getEmail();
            if (email != null && !email.isEmpty()) {
                emailService.sendSimpleEmail(email,
                        "Request rejected",
                        "Your request has been rejected by our staff. Please check your request on our website.");
            }
            return RequestMapper.INSTANCE.toDto(request);
        }

        return null;
    }

    @Secured("STAFF")
    @Override
    public RequestDto acceptRequest(UUID staffId, UUID requestId, String description) {
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new EntityNotFoundException("Request not found")
        );
        Account account = accountRepository.findById(staffId).orElseThrow(
                () -> new EntityNotFoundException("Staff not found")
        );

        request.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        request.setStaffId(account.getId());
        request.setStaffName(account.getFullName());
        request.setDescription(description);
        request.setState(RequestStateEnum.PASS);
        request = requestRepository.save(request);

        if (request != null) {
            Account readerAccount = request.getReader().getAccount();
            AccountState accountState = accountStateRepository
                    .findByNameAndStatus(READER_STATE_ACTIVE, Status.ACTIVE)
                    .orElseThrow(() -> new EntityNotFoundException("Account state not found"));
            readerAccount.setAccountState(accountState);
            readerAccount = accountRepository.save(readerAccount);
            if (readerAccount != null) {
                String email = readerAccount.getEmail();
                if (email != null && !email.isEmpty()) {
                    emailService.sendSimpleEmail(email,
                            "Request accepted",
                            "Congratulations! Your request has been accepted by our staff. Please check your request on our website.");
                }
                return RequestMapper.INSTANCE.toDto(request);
            }
        }

        return null;
    }

    private static String generateRoomId() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
