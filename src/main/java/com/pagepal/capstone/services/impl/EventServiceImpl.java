package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.bannerads.BannerAdsDto;
import com.pagepal.capstone.dtos.booking.BookingDto;
import com.pagepal.capstone.dtos.event.*;
import com.pagepal.capstone.dtos.meeting.MeetingDto;
import com.pagepal.capstone.dtos.notification.NotificationCreateDto;
import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.dtos.recording.MeetingRecordings;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.*;
import com.pagepal.capstone.mappers.BookingMapper;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.BannerAdsService;
import com.pagepal.capstone.services.EventService;
import com.pagepal.capstone.services.FirebaseMessagingService;
import com.pagepal.capstone.services.NotificationService;
import com.pagepal.capstone.services.ZoomService;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final SeminarRepository seminarRepository;
    private final BookingRepository bookingRepository;
    private final BannerAdsRepository bannerAdsRepository;
    private final BannerAdsService bannerAdsService;
    private final ReaderRepository readerRepository;
    private final SettingRepository settingRepository;
    private final CustomerRepository customerRepository;
    private final BookingStateRepository bookingStateRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;
    private final FirebaseMessagingService firebaseMessagingService;
    private final ZoomService zoomService;
    private final MeetingRepository meetingRepository;
    private final DateUtils dateUtils;

    private final String pagePalLogoUrl = "https://firebasestorage.googleapis.com/v0/b/authen-6cf1b.appspot.com/o/private_image%2F1.png?alt=media&token=56384e72-69dc-4ab3-8ede-9401b6f2f121";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public boolean canCreateEvent(UUID readerId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime startOfWeek = startOfDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // convert to date
        Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date startOfWeekDate = Date.from(startOfWeek.atZone(ZoneId.systemDefault()).toInstant());

        long eventsThisWeek = eventRepository.countByCreatedAtBetweenAndState(startOfWeekDate, nowDate, readerId, EventStateEnum.ACTIVE);

        return eventsThisWeek < 2;
    }

    @Override
    public EventDto createEvent(EventCreateDto dto, UUID readerId) {
        try {
            UUID seminarId = dto.getSeminarId();
            Seminar seminar = seminarRepository.findById(seminarId).orElse(null);

            Reader reader = readerRepository.findById(readerId).orElse(null);

            if (reader == null) {
                throw new ValidationException("Reader not found");
            }

            if (seminar == null) {
                throw new ValidationException("Seminar not found");
            }

            if (!SeminarStatus.ACCEPTED.equals(seminar.getState())) {
                throw new ValidationException("Seminar is not accepted, cannot create event");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime start = LocalDateTime.parse(dto.getStartAt(), formatter);
            LocalDateTime end = start.plusMinutes(seminar.getDuration());
            Date startAt = Date.from(start.atZone(ZoneId.systemDefault()).toInstant());
            Date endAt = Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
            //			long diff = startAt.getTime() - currentDate.getTime();
            //			long diffDays = diff / (24 * 60 * 60 * 1000);
            //			if(diffDays > 14 || diffDays < 7) {
            //				throw new ValidationException("Event start date must be within [7 - 14] days");
            //			}

            if (Boolean.FALSE.equals(canCreateEvent(reader.getId()))) {
                throw new ValidationException("Reader exceeds the limit (2) of creating seminar events for this week");
            }

            var countConflictingEvents = eventRepository.countConflictingEvents(readerId, startAt, endAt, String.valueOf(SeminarStatus.ACCEPTED),
                    String.valueOf(EventStateEnum.ACTIVE));
            if (countConflictingEvents > 0) {
                throw new ValidationException("The time slot is conflicted with other events");
            }
//            List<Event> listExistEvents = eventRepository.findBySeminarId(seminarId);
//            if (listExistEvents.size() > 0) {
//                int count = 0;
//                for (Event event : listExistEvents) {
//                    if (event.getState().equals(EventStateEnum.ACTIVE) || event.getStartAt().after(currentDate)) {
//                        count++;
//                    }
//
//                    if (event.getState().equals(EventStateEnum.ACTIVE) &&
//                            (event.getStartAt().after(startAt) || event.getStartAt().equals(startAt))) {
//                        throw new ValidationException(
//                                "Cannot create before active event or equal start date");
//                    }
//                }
//            }

            if (dto.getIsFeatured() &&
                    (dto.getAdvertiseStartAt() == null || dto.getAdvertiseStartAt().isEmpty()) &&
                    (dto.getAdvertiseEndAt() == null || dto.getAdvertiseEndAt().isEmpty())) {
                throw new ValidationException("Advertise date is required for featured event");
            }

            if (dto.getLimitCustomer() < 0) {
                throw new ValidationException("Limit customer must be greater than 0");
            }

            Meeting meeting = zoomService.createMeeting(
                    reader,
                    seminar.getTitle(),
                    seminar.getDuration(),
                    seminar.getBook().getTitle(),
                    startAt
            );

            if (meeting == null) {
                throw new RuntimeException("Failed to create meeting");
            }

            Event event = Event.builder()
                    .startAt(startAt)
                    .createdAt(dateUtils.getCurrentVietnamDate())
                    .limitCustomer(dto.getLimitCustomer())
                    .activeSlot(dto.getLimitCustomer())
                    .isFeatured(dto.getIsFeatured())
                    .seminar(seminar)
                    .meeting(meeting)
                    .price(dto.getPrice())
                    .state(EventStateEnum.ACTIVE)
                    .build();

            if (dto.getIsFeatured() &&
                    (dto.getAdvertiseStartAt() != null && !dto.getAdvertiseStartAt().isEmpty())
                    && (dto.getAdvertiseEndAt() != null && !dto.getAdvertiseEndAt().isEmpty())) {

                long diffBetweenStartAndEnd = dateFormat.parse(dto.getAdvertiseEndAt()).getTime() - dateFormat.parse(
                        dto.getAdvertiseStartAt()).getTime();
                long diffDaysBetweenStartAndEnd = diffBetweenStartAndEnd / (24 * 60 * 60 * 1000);
                if (diffDaysBetweenStartAndEnd > 7) {
                    throw new ValidationException("Advertise date must be within 7 days");
                }

                var wallet = reader.getAccount().getWallet().getTokenAmount();
                var advertisePriceSetting = settingRepository.findByKey("ADVERTISE_PRICE").orElse(null);
                if (advertisePriceSetting == null) {
                    throw new ValidationException("Advertise price not found");
                }
                var advertisePrice = Integer.parseInt(advertisePriceSetting.getValue());
                if (wallet < advertisePrice) {
                    throw new ValidationException("Not enough money to advertise");
                }

                reader.getAccount().getWallet().setTokenAmount(wallet - advertisePrice);
                readerRepository.save(reader);
                eventRepository.save(event);

                Date advertiseStartAt = dateFormat.parse(dto.getAdvertiseStartAt());
                Date advertiseEndAt = dateFormat.parse(dto.getAdvertiseEndAt());

                List<BannerAdsDto> listBannerAds = bannerAdsService.getListBannerAds();
                if (listBannerAds.size() >= 5) {
                    throw new ValidationException("Reader exceeds the limit of creating banner ads");
                }

                BannerAds bannerAds = BannerAds.builder()
                        .event(event)
                        .startAt(advertiseStartAt)
                        .endAt(advertiseEndAt)
                        .status(Status.ACTIVE)
                        .createdAt(dateUtils.getCurrentVietnamDate())
                        .build();

                bannerAdsRepository.save(bannerAds);
            } else {
                eventRepository.save(event);
            }

            return convertToDto(event);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public EventDto updateEvent(UUID id, EventUpdateDto eventDto) {
        try {
            List<Booking> bookings = bookingRepository.findAllByEventId(id);
            if (bookings.size() > 0) {
                throw new ValidationException("Event has bookings, cannot update");
            }

            Event event = eventRepository.findById(id).orElse(null);
            if (event == null) {
                throw new ValidationException("Event not found");
            }


            if (eventDto.getLimitCustomer() < 0) {
                throw new ValidationException("Cannot set limit customer less than bookings number");
            }

            event.setLimitCustomer(eventDto.getLimitCustomer());
            event.setActiveSlot(eventDto.getLimitCustomer());
            event.setStartAt(dateFormat.parse(eventDto.getStartAt()));
            event.setPrice(eventDto.getPrice());

            eventRepository.save(event);
            return convertToDto(event);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public EventDto getEvent(UUID id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            throw new ValidationException("Event not found");
        }
        return convertToDto(event);
    }

    @Override
    public EventDto deleteEvent(UUID id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            throw new ValidationException("Event not found");
        }

        List<Booking> bookings = bookingRepository.findAllByEventId(id);
        if (bookings.size() > 0) {
            throw new ValidationException("Event has bookings, cannot delete");
        }

        event.setState(EventStateEnum.INACTIVE);
        eventRepository.save(event);
        return convertToDto(event);
    }

    @Override
    public EventBookingDto bookEvent(UUID eventId, UUID customerId) {
        var customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new ValidationException("Customer not found");
        }
        var event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new ValidationException("Event not found");
        }

        if (event.getActiveSlot() <= 0) {
            throw new ValidationException("Event is full");
        }

        if (customer.getAccount().getId().equals(event.getSeminar().getReader().getAccount().getId())) {
            throw new ValidationException("Cannot book this seminar event with reader account");
        }

        Date currentTime = dateUtils.getCurrentVietnamDate();
        Date eventStartAt = event.getStartAt();

        if (currentTime.after(eventStartAt)) {
            throw new ValidationException("Cannot book event before event start time");
        }

        List<Booking> bookings = event.getBookings();

        if (bookings != null && !bookings.isEmpty()) {
            for (Booking booking : bookings) {
                if (booking.getCustomer().getId().equals(customerId) && "PENDING".equals(booking.getState().getName())) {
                    throw new ValidationException("Customer already booked this seminar event");
                }
            }
        }

        var account = customer.getAccount();
        var wallet = account.getWallet().getTokenAmount();
        int tokenLeft = wallet - event.getPrice();
        if (tokenLeft < 0) {
            throw new ValidationException("Not enough money");
        }

        Booking booking = new Booking();
        booking.setTotalPrice(event.getPrice());
        booking.setPromotionCode("");
        booking.setDescription(event.getSeminar().getDescription());
        booking.setRating(0);
        booking.setCreateAt(dateUtils.getCurrentVietnamDate());
        booking.setUpdateAt(dateUtils.getCurrentVietnamDate());
        booking.setStartAt(event.getStartAt());
        booking.setCustomer(customer);
        booking.setEvent(event);
        booking.setMeeting(event.getMeeting());
        booking.setState(
                bookingStateRepository
                        .findByName("PENDING")
                        .orElseThrow(() -> new EntityNotFoundException("State not found"))
        );
        booking = bookingRepository.save(booking);

        if (booking != null) {
            event.setActiveSlot(event.getActiveSlot() - 1);
            eventRepository.save(event);

            customer.getAccount().getWallet().setTokenAmount(tokenLeft);
            customerRepository.save(customer);

            Transaction transaction = new Transaction();
            transaction.setStatus(TransactionStatusEnum.SUCCESS);
            transaction.setCreateAt(dateUtils.getCurrentVietnamDate());
            transaction.setTransactionType(TransactionTypeEnum.BOOKING_SEMINAR_DONE);
            transaction.setCurrency(CurrencyEnum.TOKEN);
            transaction.setBooking(booking);
            transaction.setAmount(Double.valueOf(event.getPrice()));
            transaction.setWallet(customer.getAccount().getWallet());
            transactionRepository.save(transaction);

            String readerContent =
                    "Event" + " - " + event.getSeminar().getTitle() + " has booked " + "by " + customer.getFullName();
            String customerContent = "Event" + " - " + event.getSeminar().getTitle() + " has booked successfully.";

            // Save notification to reader
            NotificationCreateDto readerNotification = new NotificationCreateDto();
            readerNotification.setAccountId(event.getSeminar().getReader().getAccount().getId());
            readerNotification.setTitle("Event booking");
            readerNotification.setContent(readerContent);
            readerNotification.setNotificationRole(NotificationRoleEnum.READER);
            notificationService.createNotification(readerNotification);

            // Save notification to customer
            NotificationCreateDto customerNotification = new NotificationCreateDto();
            customerNotification.setAccountId(customer.getAccount().getId());
            customerNotification.setTitle("Event booking");
            customerNotification.setContent(customerContent);
            customerNotification.setNotificationRole(NotificationRoleEnum.CUSTOMER);
            notificationService.createNotification(customerNotification);

            String readerFcmWebToken = event.getSeminar().getReader().getAccount().getFcmWebToken();
            String customerFcmWebToken = customer.getAccount().getFcmWebToken();

            if (customerFcmWebToken != null && !customerFcmWebToken.trim().isEmpty()) {
                firebaseMessagingService.sendNotificationToDevice(
                        pagePalLogoUrl,
                        "Event booking",
                        customerContent,
                        Map.of("eventId", event.getId().toString(), "customerId", customer.getId().toString()),
                        customerFcmWebToken
                );

                if (readerFcmWebToken != null && !readerFcmWebToken.trim().isEmpty() && !readerFcmWebToken.equals(
                        customerFcmWebToken)) {
                    firebaseMessagingService.sendNotificationToDevice(
                            pagePalLogoUrl,
                            readerContent,
                            readerContent,
                            Map.of("eventId", event.getId().toString(), "customerId", customer.getId().toString()),
                            readerFcmWebToken
                    );
                }
            } else {
                if (readerFcmWebToken != null && !readerFcmWebToken.trim().isEmpty()) {
                    firebaseMessagingService.sendNotificationToDevice(
                            pagePalLogoUrl,
                            readerContent,
                            readerContent,
                            Map.of("eventId", event.getId().toString(), "customerId", customer.getId().toString()),
                            readerFcmWebToken
                    );
                }
            }

            // Send notification to mobile (reader/customer)
            String customerFcmMobileToken = customer.getAccount().getFcmMobileToken();

            if (customerFcmMobileToken != null && !customerFcmMobileToken.trim().isEmpty()) {
                firebaseMessagingService.sendNotificationToDevice(
                        pagePalLogoUrl,
                        "Event booking",
                        customerContent,
                        Map.of("eventId", event.getId().toString(), "customerId", customer.getId().toString()),
                        customerFcmMobileToken
                );
            }
        }

        EventBookingDto eventBookingDto = new EventBookingDto();
        eventBookingDto.setEvent(event);
        eventBookingDto.setBooking(booking);
        return eventBookingDto;
    }

    @Override
    public EventDto advertiseEvent(UUID eventId, UUID readerId, String advertiseAt) {
        try {
            Event event = eventRepository.findById(eventId).orElse(null);
            if (event == null) {
                throw new ValidationException("Event not found");
            }

            Reader reader = readerRepository.findById(readerId).orElse(null);
            if (reader == null) {
                throw new ValidationException("Reader not found");
            }

            if (advertiseAt == null || advertiseAt.isEmpty()) {
                throw new ValidationException("Advertise date is required");
            }

            Date eventStartAt = event.getStartAt();
            Date advertiseDate = dateFormat.parse(advertiseAt);
            Date currentDate = dateUtils.getCurrentVietnamDate();

            if (advertiseDate.after(eventStartAt)) {
                throw new ValidationException("Advertise date must be before event start date");
            }

            if (advertiseDate.before(currentDate)) {
                throw new ValidationException("Advertise date must be after current date");
            }

            // if advertise date is before event start date more than 2 weeks, then throw exception
            long diff = eventStartAt.getTime() - advertiseDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (diffDays > 14) {
                throw new ValidationException("Advertise date must be within 2 weeks from event start date");
            }

            BannerAds bannerAds = BannerAds.builder()
                    .event(event)
                    .startAt(advertiseDate)
                    .createdAt(dateUtils.getCurrentVietnamDate())
                    .build();

            bannerAdsRepository.save(bannerAds);
            return convertToDto(event);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public ListEventDto getAllEventBySeminarId(UUID seminarId, Integer page, Integer pageSize, String sort) {
        Pageable pageable = createPageable(page, pageSize, sort);
        Page<Event> events = eventRepository.findBySeminarId(seminarId, EventStateEnum.ACTIVE, pageable);
        var listEventDto = new ListEventDto();

        if (events == null) {
            listEventDto.setList(Collections.emptyList());
            listEventDto.setPagination(null);
        } else {
            var pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(events.getTotalPages());
            pagingDto.setTotalOfElements(events.getTotalElements());
            pagingDto.setSort(events.getSort().toString());
            pagingDto.setCurrentPage(events.getNumber());
            pagingDto.setPageSize(events.getSize());

            listEventDto.setList(events.map(this::convertToDto).getContent());
            listEventDto.setPagination(pagingDto);
        }
        return listEventDto;
    }

    @Override
    public ListEventDto getAllEvent(Integer page, Integer pageSize, String sort) {
        Date currentDate = new Date();
        Pageable pageable = createPageable(page, pageSize, sort);
        Page<Event> events = eventRepository.findByStateAndStartAtAfter(EventStateEnum.ACTIVE, currentDate, pageable);
        var listEventDto = new ListEventDto();

        if (events == null) {
            listEventDto.setList(Collections.emptyList());
            listEventDto.setPagination(null);
        } else {
            var pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(events.getTotalPages());
            pagingDto.setTotalOfElements(events.getTotalElements());
            pagingDto.setSort(events.getSort().toString());
            pagingDto.setCurrentPage(events.getNumber());
            pagingDto.setPageSize(events.getSize());

            listEventDto.setList(events.map(this::convertToDto).getContent());
            listEventDto.setPagination(pagingDto);

        }
        return listEventDto;
    }

    @Override
    public ListEventDto getAllEventNotJoinByCustomer(UUID customerId, Integer page, Integer pageSize, String sort) {
        Pageable pageable = createPageable(page, pageSize, sort);
        Page<Event> events = eventRepository.findAllEventNotJoinByCustomer(customerId, EventStateEnum.ACTIVE, dateUtils.getCurrentVietnamDate(), pageable);
        var listEventDto = new ListEventDto();

        if (events == null) {
            listEventDto.setList(Collections.emptyList());
            listEventDto.setPagination(null);
        } else {
            var pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(events.getTotalPages());
            pagingDto.setTotalOfElements(events.getTotalElements());
            pagingDto.setSort(events.getSort().toString());
            pagingDto.setCurrentPage(events.getNumber());
            pagingDto.setPageSize(events.getSize());

            listEventDto.setList(events.map(this::convertToDto).getContent());
            listEventDto.setPagination(pagingDto);
        }

        return listEventDto;
    }

    @Override
    public ListEventDto getAllEventByReader(UUID readerId, Integer page, Integer pageSize, String sort) {
        Pageable pageable = createPageable(page, pageSize, sort);
        Page<Event> events = eventRepository.findAllByReaderId(readerId, pageable);
        var listEventDto = new ListEventDto();

        if (events == null) {
            listEventDto.setList(Collections.emptyList());
            listEventDto.setPagination(null);
        } else {
            var pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(events.getTotalPages());
            pagingDto.setTotalOfElements(events.getTotalElements());
            pagingDto.setSort(events.getSort().toString());
            pagingDto.setCurrentPage(events.getNumber());
            pagingDto.setPageSize(events.getSize());

            listEventDto.setList(events.map(this::convertToDto).getContent());
            listEventDto.setPagination(pagingDto);
        }


        return listEventDto;
    }

    @Override
    public ListEventDto getAllActiveEventByReader(UUID readerId, Integer page, Integer pageSize, String sort) {
        Pageable pageable = createPageable(page, pageSize, sort);
        Date currentDate = new Date();
        Page<Event> events = eventRepository.findAllEventActiveByReaderId(readerId, EventStateEnum.ACTIVE, currentDate,
                pageable);
        var listEventDto = new ListEventDto();

        if (events == null) {
            listEventDto.setList(Collections.emptyList());
            listEventDto.setPagination(null);
        } else {
            var pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(events.getTotalPages());
            pagingDto.setTotalOfElements(events.getTotalElements());
            pagingDto.setSort(events.getSort().toString());
            pagingDto.setCurrentPage(events.getNumber());
            pagingDto.setPageSize(events.getSize());

            listEventDto.setList(events.map(this::convertToDto).getContent());
            listEventDto.setPagination(pagingDto);
        }

        return listEventDto;
    }

    @Override
    public ListEventDto getAllActiveEvent(Integer page, Integer pageSize, String sort) {
        Pageable pageable = createPageable(page, pageSize, sort);
        Date currentDate = new Date();
        Page<Event> events = eventRepository.findAllActiveEvent(EventStateEnum.ACTIVE, currentDate, pageable);
        var listEventDto = new ListEventDto();

        if (events == null) {
            listEventDto.setList(Collections.emptyList());
            listEventDto.setPagination(null);
        } else {
            var pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(events.getTotalPages());
            pagingDto.setTotalOfElements(events.getTotalElements());
            pagingDto.setSort(events.getSort().toString());
            pagingDto.setCurrentPage(events.getNumber());
            pagingDto.setPageSize(events.getSize());

            listEventDto.setList(events.map(this::convertToDto).getContent());
            listEventDto.setPagination(pagingDto);
        }

        return listEventDto;
    }

    private EventDto convertToDto(Event event) {
        if (event == null) {
            return null;
        }
        return EventDto.builder()
                .id(event.getId())
                .startAt(dateFormat.format(event.getStartAt()))
                .createdAt(dateFormat.format(event.getCreatedAt()))
                .limitCustomer(event.getLimitCustomer())
                .activeSlot(event.getActiveSlot())
                .isFeatured(event.getIsFeatured())
                .state(event.getState())
                .price(event.getPrice())
                .seminar(event.getSeminar())
                .bookings(
                        event.getBookings() == null
                                ? Collections.emptyList()
                                : event.getBookings().stream().map(this::toDtoIncludeRecording).toList()
                )
                .build();
    }

    private Pageable createPageable(Integer page, Integer pageSize, String sort) {
        if (page == null || page < 0) {
            page = 0;
        }

        if (pageSize == null || pageSize < 0) {
            pageSize = 10;
        }

        if (sort != null && sort.equals("desc")) {
            return PageRequest.of(page, pageSize, Sort.by("startAt").descending());
        } else {
            return PageRequest.of(page, pageSize, Sort.by("startAt").ascending());
        }
    }

    private BookingDto toDtoIncludeRecording(Booking booking) {
        BookingDto bookingDto = BookingMapper.INSTANCE.toDto(booking);

//		MeetingDto meeting = bookingDto.getMeeting();
//
//		MeetingRecordings recording = zoomService.getListRecordingByMeetingId(booking.getMeeting().getMeetingCode());
//		meeting.setRecordings(recording);
//		bookingDto.setMeeting(meeting);
        return bookingDto;
    }
}
