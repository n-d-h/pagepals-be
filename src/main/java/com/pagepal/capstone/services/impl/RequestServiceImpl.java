package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.notification.NotificationCreateDto;
import com.pagepal.capstone.dtos.request.RequestDto;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.NotificationRoleEnum;
import com.pagepal.capstone.enums.RequestStateEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.RequestMapper;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.*;
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
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final String READER_STATE_ACTIVE = "READER_ACTIVE";
    private final String CUSTOMER_STATE_ACTIVE = "ACTIVE";

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final DateUtils dateUtils;
    private final RoleRepository roleRepository;
    private final ZoomService zoomService;
    private final FirebaseMessagingService firebaseMessagingService;
    private final NotificationService notificationService;

    private final String pagePalLogoUrl = "https://firebasestorage.googleapis.com/v0/b/authen-6cf1b.appspot.com/o/private_image%2F1.png?alt=media&token=56384e72-69dc-4ab3-8ede-9401b6f2f121";

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

        Meeting meeting = zoomService.createInterviewMeeting("Interview for " + request.getReader().getAccount().getEmail(),
                120, "Interview become reader", startDate);
        if (meeting == null) {
            throw new RuntimeException("Failed to create meeting");
        }
        request.setMeetingCode(meeting.getMeetingCode());
        request.setMeetingPassword(meeting.getPassword());
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
            String subject = "[PagePals]: Interview schedule";
            String emailBody = getUpdateRequestInterviewEmailBody(reader, startDate, requestId);
            if (email != null && !email.isEmpty()) {
                emailService.sendSimpleEmail(email, subject, emailBody);

                NotificationCreateDto notificationCreateDto = NotificationCreateDto.builder()
                        .accountId(reader.getAccount().getId())
                        .content("You have an interview schedule at: " + startDate + "; Please check your request on our website.")
                        .title("Interview schedule")
                        .notificationRole(NotificationRoleEnum.CUSTOMER)
                        .build();

                notificationService.createNotification(notificationCreateDto);

                String readerFcmMobileToken = reader.getAccount().getFcmMobileToken();
                String readerFcmWebToken = reader.getAccount().getFcmWebToken();

                if (readerFcmMobileToken != null && !readerFcmMobileToken.trim().isEmpty()) {
                    firebaseMessagingService.sendNotificationToDevice(
                            pagePalLogoUrl,
                            "Interview schedule",
                            "You have an interview schedule at: " + startDate + "; Please check your email for more details.",
                            Map.of("requestId", request.getId().toString()),
                            readerFcmMobileToken
                    );
                }

                if (readerFcmWebToken != null && !readerFcmWebToken.trim().isEmpty()) {
                    firebaseMessagingService.sendNotificationToDevice(
                            pagePalLogoUrl,
                            "You have an interview schedule at: " + startDate + "; Please check your email for more details.",
                            "You have an interview schedule at: " + startDate + "; Please check your email for more details.",
                            Map.of("requestId", request.getId().toString()),
                            readerFcmWebToken
                    );
                }
            }
            return RequestMapper.INSTANCE.toDto(request);
        }
        return null;
    }

    private static String getUpdateRequestInterviewEmailBody(Reader reader, Date startDate, UUID requestId) {
        String date = new SimpleDateFormat("MMM dd, yyyy").format(startDate);
        String time = new SimpleDateFormat("hh:mm:ss").format(startDate);
        String location = "https://pagepals-fe.vercel.app/main/become-a-reader";
        return """
                Dear %s,
                                    
                Thank you for your interest in joining PagePals! We are excited to inform you that an interview has been scheduled for you as part of our application process.
                                
                Interview Details:
                - Date: %s
                - Time: %s
                - Location: %s
                - Request ID: %s
                                    
                During the interview, we will discuss your qualifications, skills, and experience relevant to the position, as well as the responsibilities and expectations associated with the role.
                                    
                Please make sure to join online meeting on time and bring any necessary documents or materials. If you have any questions or need further assistance, feel free to contact us.
                                    
                We look forward to meeting you and discussing how you can contribute to our team at PagePals.
                                    
                Best regards,
                The PagePals Team
                                    
                """
                .formatted(
                        reader.getAccount().getUsername(),
                        date,
                        time,
                        location,
                        requestId.toString()
                );
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
            Account readerAccount = request.getReader().getAccount();
            readerAccount.setAccountState(accountStateRepository
                    .findByNameAndStatus(CUSTOMER_STATE_ACTIVE, Status.ACTIVE)
                    .orElseThrow(() -> new EntityNotFoundException("Account state not found")));
            accountRepository.save(readerAccount);

            Reader reader = request.getReader();
            String email = reader.getAccount().getEmail();
            String website = "https://pagepals-fe.vercel.app";
            if (email != null && !email.isEmpty()) {
                String subject = "[PagePals]: Request rejected";
                String body = """
                        Dear %s,

                        We regret to inform you that your recent request has been rejected by our staff. Please find the details below:

                        Request ID: %s
                        Description: %s

                        If you have any questions or need further clarification regarding this decision, please feel free to reach out to us. We're here to assist you.
                        Our website: %s

                        Thank you for your understanding.

                        Best regards,
                        The PagePals Team
                        """.formatted(reader.getAccount().getUsername(), requestId.toString(), description, website);

                emailService.sendSimpleEmail(email, subject, body);

                NotificationCreateDto notificationCreateDto = NotificationCreateDto.builder()
                        .accountId(reader.getAccount().getId())
                        .content("Your request has been rejected by our staff. Please check your request on our website.")
                        .title("Request rejected")
                        .notificationRole(NotificationRoleEnum.CUSTOMER)
                        .build();

                notificationService.createNotification(notificationCreateDto);

                String readerFcmMobileToken = reader.getAccount().getFcmMobileToken();
                String readerFcmWebToken = reader.getAccount().getFcmWebToken();

                if (readerFcmMobileToken != null && !readerFcmMobileToken.trim().isEmpty()) {
                    firebaseMessagingService.sendNotificationToDevice(
                            pagePalLogoUrl,
                            "Request rejected",
                            "Your request to be reader has been rejected by our staff.",
                            Map.of("requestId", request.getId().toString()),
                            readerFcmMobileToken
                    );
                }

                if (readerFcmWebToken != null && !readerFcmWebToken.trim().isEmpty()) {
                    firebaseMessagingService.sendNotificationToDevice(
                            pagePalLogoUrl,
                            "Your request to be reader has been rejected by our staff.",
                            "Your request to be reader has been rejected by our staff.",
                            Map.of("requestId", request.getId().toString()),
                            readerFcmWebToken
                    );
                }
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
            Role role = roleRepository
                    .findByName("READER")
                    .orElseThrow(() -> new EntityNotFoundException("Role not found"));
            readerAccount.setAccountState(accountState);
            readerAccount.setRole(role);
            readerAccount = accountRepository.save(readerAccount);
            if (readerAccount != null) {
                String email = readerAccount.getEmail();
                String website = "https://pagepals-fe.vercel.app";
                if (email != null && !email.isEmpty()) {
                    String subject = "[PagePals]: Request Approved";
                    String body = """
                            Dear %s,

                            We're delighted to inform you that your recent request has been approved by our staff. Please find the details below:

                            Request ID: %s
                            Description: %s

                            Your request has been successfully processed. You can now view the updated status on our website: %s.

                            If you have any further questions or need assistance, please don't hesitate to reach out to us. We're here to help.

                            Thank you for choosing us.

                            Best regards,
                            The PagePals Team
                            """.formatted(readerAccount.getUsername(), requestId.toString(), description, website);

                    emailService.sendSimpleEmail(email, subject, body);

                    NotificationCreateDto notificationCreateDto = NotificationCreateDto.builder()
                            .accountId(readerAccount.getId())
                            .content("Congratulations! Your request has been accepted by our staff.")
                            .title("Request accepted")
                            .notificationRole(NotificationRoleEnum.CUSTOMER)
                            .build();

                    notificationService.createNotification(notificationCreateDto);

                    String readerFcmMobileToken = readerAccount.getFcmMobileToken();
                    String readerFcmWebToken = readerAccount.getFcmWebToken();

                    if (readerFcmMobileToken != null && !readerFcmMobileToken.trim().isEmpty()) {
                        firebaseMessagingService.sendNotificationToDevice(
                                pagePalLogoUrl,
                                "Request accepted",
                                "Congratulations! Your request has been accepted by our staff.",
                                Map.of("requestId", request.getId().toString()),
                                readerFcmMobileToken
                        );
                    }

                    if (readerFcmWebToken != null && !readerFcmWebToken.trim().isEmpty()) {
                        firebaseMessagingService.sendNotificationToDevice(
                                pagePalLogoUrl,
                                "Congratulations! Your request has been accepted by our staff.",
                                "Congratulations! Your request has been accepted by our staff.",
                                Map.of("requestId", request.getId().toString()),
                                readerFcmWebToken
                        );
                    }
                }
                return RequestMapper.INSTANCE.toDto(request);
            }
        }

        return null;
    }

    @Override
    public RequestDto getRequestByReaderId(UUID readerId) {
        List<RequestStateEnum> listState = Arrays.asList(RequestStateEnum.INTERVIEW_PENDING, RequestStateEnum.ANSWER_CHECKING);
        Request request = requestRepository.findByReaderIdAndStates(readerId, listState).orElse(null);
        return request == null ? null : RequestMapper.INSTANCE.toDto(request);
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
