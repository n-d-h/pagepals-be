package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.interview.InterviewDto;
import com.pagepal.capstone.dtos.notification.NotificationCreateDto;
import com.pagepal.capstone.dtos.request.RequestDto;
import com.pagepal.capstone.dtos.workingtime.TimeSlot;
import com.pagepal.capstone.dtos.workingtime.WorkingDate;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeListRead;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.*;
import com.pagepal.capstone.mappers.InterviewMapper;
import com.pagepal.capstone.mappers.RequestMapper;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.*;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    private final InterviewRepository interviewRepository;

    @Secured("STAFF")
    @Override
    public List<RequestDto> getListRequest() {
        List<Request> requests = requestRepository.findAll();
        return requests.stream().map(RequestMapper.INSTANCE::toDto).toList();
    }

    @Override
    public RequestDto getRequestById(UUID requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new EntityNotFoundException("Request not found")
        );

        return getRequestWithLastRequest(request);
    }

    @Secured("STAFF")
    @Override
    public RequestDto updateRequestInterview(UUID staffId, UUID requestId, String interviewAt, String description) throws ParseException {
//        Request request = requestRepository.findById(requestId).orElseThrow(
//                () -> new EntityNotFoundException("Request not found")
//        );
//        Account account = accountRepository.findById(staffId).orElseThrow(
//                () -> new EntityNotFoundException("Staff not found")
//        );
//
//        Date startDate = dateFormat.parse(interviewAt);
//
//        Meeting meeting = zoomService.createInterviewMeeting("Interview for " + request.getReader().getAccount().getEmail(),
//                120, "Interview become reader", startDate);
//        if (meeting == null) {
//            throw new RuntimeException("Failed to create meeting");
//        }
//        request.setMeetingCode(meeting.getMeetingCode());
//        request.setMeetingPassword(meeting.getPassword());
//        request.setUpdatedAt(dateUtils.getCurrentVietnamDate());
//        request.setInterviewAt(startDate);
//        request.setStaffId(account.getId());
//        request.setStaffName(account.getFullName());
//        request.setDescription(description);
//        request.setState(RequestStateEnum.INTERVIEW_PENDING);
//
//        request = requestRepository.save(request);
//
//        if (request != null) {
//            Reader reader = request.getReader();
//            String email = reader.getAccount().getEmail();
//            String subject = "[PagePals]: Interview schedule";
//            String emailBody = getUpdateRequestInterviewEmailBody(reader, startDate, requestId);
//            if (email != null && !email.isEmpty()) {
//                emailService.sendSimpleEmail(email, subject, emailBody);
//
//                NotificationCreateDto notificationCreateDto = NotificationCreateDto.builder()
//                        .accountId(reader.getAccount().getId())
//                        .content("You have an interview schedule at: " + startDate + "; Please check your request on our website.")
//                        .title("Interview schedule")
//                        .notificationRole(NotificationRoleEnum.CUSTOMER)
//                        .build();
//
//                notificationService.createNotification(notificationCreateDto);
//
//                String readerFcmMobileToken = reader.getAccount().getFcmMobileToken();
//                String readerFcmWebToken = reader.getAccount().getFcmWebToken();
//
//                if (readerFcmMobileToken != null && !readerFcmMobileToken.trim().isEmpty()) {
//                    firebaseMessagingService.sendNotificationToDevice(
//                            pagePalLogoUrl,
//                            "Interview schedule",
//                            "You have an interview schedule at: " + startDate + "; Please check your email for more details.",
//                            Map.of("requestId", request.getId().toString()),
//                            readerFcmMobileToken
//                    );
//                }
//
//                if (readerFcmWebToken != null && !readerFcmWebToken.trim().isEmpty()) {
//                    firebaseMessagingService.sendNotificationToDevice(
//                            pagePalLogoUrl,
//                            "You have an interview schedule at: " + startDate + "; Please check your email for more details.",
//                            "You have an interview schedule at: " + startDate + "; Please check your email for more details.",
//                            Map.of("requestId", request.getId().toString()),
//                            readerFcmWebToken
//                    );
//                }
//            }
//            return RequestMapper.INSTANCE.toDto(request);
//        }
        return null;
    }


    private static String getUpdateRequestInterviewEmailBody(Reader reader) {
        String location = "https://pagepals-fe.vercel.app/main/become-a-reader";
        return """
                Dear %s,
                                    
                Thank you for your interest in our PagePals platform. I am happy to receive your application to become our reader.
                 
                Regarding your inquiry, I'd like to suggest we have a quick conversation (10 - 30 minutes) so I can better understand what you are looking for and discuss more about how PagePals can help you with your needs.              
                               
                The link below provides my calendar availability over the coming days. Feel free to select a time that works best for you.
                %s
                                    
                During the interview, we will discuss your qualifications, skills, and experience relevant to the position, as well as the responsibilities and expectations associated with the role.
                                    
                Please make sure to join online meeting on time and bring any necessary documents or materials. If you have any questions or need further assistance, feel free to contact us.
                                    
                We look forward to meeting you and discussing how you can contribute to our team at PagePals.
                                    
                Best regards,
                The PagePals Team
                                    
                """
                .formatted(
                        reader.getAccount().getUsername(),
                        location
                );
    }

    @Secured("STAFF")
    @Override
    public RequestDto rejectRequest(UUID staffId, UUID requestId, String reason, String description) {
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new EntityNotFoundException("Request not found")
        );
        Account account = accountRepository.findById(staffId).orElseThrow(
                () -> new EntityNotFoundException("Staff not found")
        );

        request.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        request.setStaffId(account.getId());
        request.setStaffName(account.getFullName());
        request.setRejectReason(reason);
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
                        Reject Reason: %s

                        If you have any questions or need further clarification regarding this decision, please feel free to reach out to us. We're here to assist you.
                        Our website: %s

                        Thank you for your understanding.

                        Best regards,
                        The PagePals Team
                        """.formatted(reader.getAccount().getUsername(), requestId.toString(), reason, website);

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
            return getRequestWithLastRequest(request);
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
                return getRequestWithLastRequest(request);
            }
        }

        return null;
    }

    @Override
    public RequestDto getRequestByReaderId(UUID readerId) {
//        List<RequestStateEnum> listState = Arrays.asList(RequestStateEnum.INTERVIEW_PENDING, RequestStateEnum.ANSWER_CHECKING);
//        Request request = requestRepository.findByReaderIdAndStates(readerId, listState).orElse(null);

        Reader reader = readerRepository.findById(readerId).orElseThrow(
                () -> new EntityNotFoundException("Reader not found")
        );

        List<Request> requests = reader.getReaderRequests()
                .stream()
                .map(Reader::getRequest)
                .toList();

        Optional<Request> requestToRemove = requests.stream()
                .filter(request -> !request.getState().equals(RequestStateEnum.PASS) && !request.getState().equals(RequestStateEnum.REJECT))
                .findFirst();

        List<Request> updatedRequests = requestToRemove
                .map(request -> requests.stream().filter(r -> !r.equals(request)).toList())
                .orElse(requests);

        Request removedRequest = requestToRemove.orElse(null);

        RequestDto request = new RequestDto();

        if (removedRequest == null) {
            request.setLastRequests(updatedRequests.stream().map(RequestMapper.INSTANCE::toDto).toList());
        }else{
            request = RequestMapper.INSTANCE.toDto(removedRequest);
            request.setLastRequests(updatedRequests.stream().map(RequestMapper.INSTANCE::toDto).toList());
        }

        return request;
    }

    @Override
    public RequestDto updateRequestToScheduling(UUID staffId, UUID requestId, String description) {

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
        request.setState(RequestStateEnum.INTERVIEW_SCHEDULING);
        request = requestRepository.save(request);

        if (request != null) {
            Reader reader = request.getReader().getReaderRequestReference();
            String email = reader.getAccount().getEmail();
            String subject = "[PagePals]: Interview schedule";
            String emailBody = getUpdateRequestInterviewEmailBody(reader);
            if (email != null && !email.isEmpty()) {
                emailService.sendSimpleEmail(email, subject, emailBody);

                NotificationCreateDto notificationCreateDto = NotificationCreateDto.builder()
                        .accountId(reader.getAccount().getId())
                        .content("Your registration is accepted! Please check your request.")
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
                            "Your registration is accepted! Please check your request.",
                            Map.of("requestId", request.getId().toString()),
                            readerFcmMobileToken
                    );
                }

                if (readerFcmWebToken != null && !readerFcmWebToken.trim().isEmpty()) {
                    firebaseMessagingService.sendNotificationToDevice(
                            pagePalLogoUrl,
                            "Request Updated!",
                            "Your registration is accepted! Please check your request.",
                            Map.of("requestId", request.getId().toString()),
                            readerFcmWebToken
                    );
                }
            }
            return getRequestWithLastRequest(request);
        }

        return null;
    }

    @Override
    public WorkingTimeListRead getWorkingTimeListByStaff(UUID staffId) {
        Account staffAccount = accountRepository.findById(staffId).orElseThrow(
                () -> new EntityNotFoundException("Staff not found")
        );

        List<Request> requests = requestRepository.findByStaffIdAndState(staffId, RequestStateEnum.INTERVIEW_SCHEDULING);
        if (requests == null || requests.isEmpty()) {
            return null;
        }
        List<Interview> interviews = new ArrayList<>();
        for (Request request : requests) {
            request.getInterviews().stream().filter(interview1 -> InterviewStateEnum.PENDING.equals(interview1.getState())).findFirst().ifPresent(interviews::add);
        }

        if (interviews.isEmpty()) {
            return null;
        }

        return divideWorkingTimes(interviews);
    }

    @Override
    public InterviewDto updateInterviewTime(UUID requestId, String interviewAt) throws ParseException {
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new EntityNotFoundException("Request not found")
        );
        if (request.getInterviews() != null || request.getInterviews().isEmpty()) {
            request.getInterviews().stream()
                    .filter(interview ->
                            InterviewStateEnum.PENDING.equals(interview.getState())).findFirst().ifPresent(interview -> {
                        throw new ValidationException("Interview is pending");
                    });
        }
        Date startDate = dateFormat.parse(interviewAt);

        Meeting meeting = zoomService.createInterviewMeeting("Interview for " + request.getReader().getReaderRequestReference().getAccount().getEmail(),
                120, "Interview become reader", startDate);
        if (meeting == null) {
            throw new RuntimeException("Failed to create meeting");
        }

        request.setState(RequestStateEnum.INTERVIEW_PENDING);
        request.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        request = requestRepository.save(request);

        if (request != null) {
            Interview interview = new Interview();
            interview.setInterviewAt(startDate);
            interview.setState(InterviewStateEnum.PENDING);
            interview.setRequest(request);
            interview.setMeeting(meeting);
            interview.setResult(InterviewResultEnum.PENDING);
            interview.setState(InterviewStateEnum.PENDING);
            interview = interviewRepository.save(interview);
            return InterviewMapper.INSTANCE.toDto(interview);
        }

        return null;
    }

    @Override
    public List<RequestDto> getListRequestByReaderId(UUID readerId) {
        Reader reader = readerRepository.findById(readerId).orElseThrow(
                () -> new EntityNotFoundException("Reader not found")
        );

        List<Request> requests = reader.getReaderRequests().stream().map(Reader::getRequest).toList();
        return requests.stream().map(RequestMapper.INSTANCE::toDto).toList();
    }

    @Override
    public InterviewDto updateInterview(UUID interviewId, InterviewStateEnum interviewStateEnum, InterviewResultEnum result, String note) {

        Interview interview = interviewRepository.findById(interviewId).orElseThrow(
                () -> new EntityNotFoundException("Interview not found")
        );

        interview.setState(interviewStateEnum);
        interview.setResult(result);
        interview.setNote(note);
        interview = interviewRepository.save(interview);

        return null;
    }

    private static WorkingTimeListRead divideWorkingTimes(List<Interview> interviews) {
        // Group the working times by date
        Map<String, List<Interview>> groupedInterviews = interviews.stream()
                .collect(Collectors.groupingBy(interview -> {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    return dateFormat.format(interview.getInterviewAt());
                }));

        // Create WorkingTimeListRead object
        WorkingTimeListRead workingTimeListRead = new WorkingTimeListRead();
        workingTimeListRead.setWorkingDates(new ArrayList<>());

        // Iterate over the grouped working times and create WorkingDate objects
        for (Map.Entry<String, List<Interview>> entry : groupedInterviews.entrySet()) {
            String date = entry.getKey();
            List<Interview> interviewsForDate = entry.getValue();

            // Create WorkingDate object
            WorkingDate workingDate = new WorkingDate();
            workingDate.setDate(date + " 00:00:00");
            workingDate.setTimeSlots(new ArrayList<>());

            // Iterate over the interviews for the date and create TimeSlot objects
            for (Interview interview : interviewsForDate) {
                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setId(interview.getId());
                timeSlot.setStartTime(getTime(interview.getInterviewAt()));
                Calendar endTime = Calendar.getInstance();
                endTime.setTime(interview.getInterviewAt());
                endTime.add(Calendar.MINUTE, 30);
                timeSlot.setEndTime(getTime(endTime.getTime()));

                timeSlot.setIsSeminar(interview.getId() == null);
                workingDate.getTimeSlots().add(timeSlot);
            }

            // Sort the time slots by start time
            workingDate.getTimeSlots().sort(Comparator.comparing(TimeSlot::getStartTime));

            // Add WorkingDate object to WorkingTimeListRead
            workingTimeListRead.getWorkingDates().add(workingDate);
        }

        // Sort the working dates by date
        workingTimeListRead.getWorkingDates().sort(Comparator.comparing(WorkingDate::getDate));

        return workingTimeListRead;
    }

    private static String getTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        return String.format("%02d:%02d:%02d", hour, minute, second); // Example start time
    }

    private RequestDto getRequestWithLastRequest(Request request) {

        List<Request> requests = requestRepository
                .findByReaderReferenceIdExcludingRequest(
                        request.getReader().getReaderRequestReference().getId(),
                        request.getId()
                );
        RequestDto requestDto = RequestMapper.INSTANCE.toDto(request);
        requestDto.setLastRequests(requests.stream().map(RequestMapper.INSTANCE::toDto).toList());

        return requestDto;
    }

    private RequestDto getRequestWithLastRequest2(Request request) {

        List<Request> requests = new ArrayList<>(request.getReader().getReaderRequestReference().getReaderRequests()
                .stream().map(Reader::getRequest).toList());

        requests.remove(request);

        RequestDto requestDto = RequestMapper.INSTANCE.toDto(request);
        requestDto.setLastRequests(requests.stream().map(RequestMapper.INSTANCE::toDto).toList());

        return requestDto;
    }
}
