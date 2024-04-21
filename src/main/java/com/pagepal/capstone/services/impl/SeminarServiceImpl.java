package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.googlebook.GoogleBook;
import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.dtos.seminar.*;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.*;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.BookService;
import com.pagepal.capstone.services.SeminarService;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class SeminarServiceImpl implements SeminarService {
    private final BookingStateRepository bookingStateRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final SeminarRepository seminarRepository;
    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;
    private final BookingRepository bookingRepository;
    private final MeetingRepository meetingRepository;
    private final ZoomServiceImpl zoomService;
    private final DateUtils dateUtils;
    private final BookService bookService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Secured({"READER"})
    @Override
    public SeminarDto createSeminar(SeminarCreateDto seminarCreateDto) {
        try {
            Reader reader = this.checkWorkingTime(seminarCreateDto.getReaderId(), seminarCreateDto, null, null);

            Book book = bookRepository.findByExternalId(seminarCreateDto.getBook().getId()).orElse(null);

            if (book == null) {
                book = bookService.createNewBook(seminarCreateDto.getBook());
            }

            Date startTime = dateFormat.parse(seminarCreateDto.getStartTime());

            Meeting meeting = zoomService
                    .createMeeting(
                            reader,
                            seminarCreateDto.getDescription(),
                            seminarCreateDto.getDuration(),
                            seminarCreateDto.getTitle(),
                            startTime
                    );

            if (meeting == null) throw new RuntimeException("Cannot create meeting");

            Seminar seminar = Seminar.builder()
                    .title(seminarCreateDto.getTitle())
                    .limitCustomer(seminarCreateDto.getLimitCustomer())
                    .activeSlot(seminarCreateDto.getActiveSlot())
                    .description(seminarCreateDto.getDescription())
                    .imageUrl(seminarCreateDto.getImageUrl())
                    .duration(seminarCreateDto.getDuration())
                    .price(seminarCreateDto.getPrice())
                    .status(SeminarStatus.ACTIVE)
                    .createdAt(dateUtils.getCurrentVietnamDate())
                    .updatedAt(dateUtils.getCurrentVietnamDate())
                    .startTime(startTime)
                    .reader(reader)
                    .book(book)
                    .meeting(meeting)
                    .build();

            seminar = seminarRepository.save(seminar);

            if (seminar == null) throw new RuntimeException("Cannot create seminar");

            return toSeminarDto(seminar);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create seminar:" + e.getMessage());
        }
    }

    @Secured({"READER"})
    @Override
    public SeminarDto updateSeminar(UUID readerId, UUID id, SeminarUpdateDto seminarUpdateDto) {
        try {
            Seminar seminar = seminarRepository.findById(id).orElse(null);
            if (seminar == null) {
                throw new RuntimeException("Seminar not found");
            }


            List<Booking> bookings = seminar.getBookings();
            if (bookings.size() > 0) {
                throw new ValidationException("Seminar is booked, Cannot update");
            }

            Reader reader = this.checkWorkingTime(readerId, null, id, seminarUpdateDto);

            Date startTime = dateFormat.parse(seminarUpdateDto.getStartTime());

            Meeting meeting = zoomService
                    .createMeeting(
                            reader,
                            seminarUpdateDto.getDescription(),
                            seminarUpdateDto.getDuration(),
                            seminarUpdateDto.getTitle(),
                            startTime
                    );

            if (meeting == null) throw new RuntimeException("Cannot create meeting");

            seminar.setTitle(seminarUpdateDto.getTitle());
            seminar.setLimitCustomer(seminarUpdateDto.getLimitCustomer());
            seminar.setActiveSlot(seminarUpdateDto.getActiveSlot());
            seminar.setDescription(seminarUpdateDto.getDescription());
            seminar.setImageUrl(seminarUpdateDto.getImageUrl());
            seminar.setDuration(seminarUpdateDto.getDuration());
            seminar.setPrice(seminarUpdateDto.getPrice());
            seminar.setUpdatedAt(dateUtils.getCurrentVietnamDate());
            seminar.setStartTime(startTime);
            seminar.setReader(reader);
            seminar.setMeeting(meeting);

            var data = seminarRepository.save(seminar);
            return toSeminarDto(data);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public SeminarDto getSeminar(UUID id) {
        var data = seminarRepository.findById(id).orElseThrow();
        return toSeminarDto(data);
    }

    @Override
    public ListSeminarDto getSeminarList(Integer page, Integer pageSize, String sort) {
        Pageable pageable = createPageable(page, pageSize, sort);
        Page<Seminar> data = seminarRepository.findAll(pageable);
        return this.mapSeminarsToDto(data);
    }

    @Override
    public ListSeminarDto getSeminarListByReaderId(UUID readerId, Integer page, Integer pageSize, String sort) {
        Pageable pageable = createPageable(page, pageSize, sort);
        Page<Seminar> data = seminarRepository.findAllByReaderId(readerId, pageable);
        return this.mapSeminarsToDto(data);
    }

    @Secured({"READER"})
    @Override
    public SeminarDto deleteSeminar(UUID id) {
        var seminar = seminarRepository.findById(id).orElse(null);
        if (seminar == null) {
            throw new ValidationException("Seminar not found");
        }
        var meeting = meetingRepository.findBySeminarId(id).orElse(null);
        if (meeting != null) {
            List<Booking> bookings = meeting.getBookings();
            if (bookings.size() > 0) {
                throw new ValidationException("Seminar is booked, Cannot delete");
            }
        }
        seminarRepository.delete(seminar);
        return toSeminarDto(seminar);
    }

    @Override
    public SeminarBookingDto bookSeminar(UUID seminarId, UUID customerId) {
        var customer = customerRepository.findById(customerId).orElse(null);
        var seminar = seminarRepository.findById(seminarId).orElse(null);

        if (customer == null || seminar == null) {
            throw new ValidationException("Customer or Seminar not found");
        }

        if (seminar.getActiveSlot() <= 0) {
            throw new RuntimeException("Seminar is full");
        }

        var meeting = seminar.getMeeting();
        List<Booking> bookings = meeting.getBookings();

        if (bookings.size() >= 0) {
            for (Booking booking : bookings) {
                if (booking.getCustomer().getId().equals(customerId)) {
                    throw new ValidationException("Customer already booked this seminar");
                }
            }
        }

        var account = customer.getAccount();
        var wallet = account.getWallet().getTokenAmount();
        int tokenLeft = wallet - seminar.getPrice();
        if (tokenLeft < 0) {
            throw new ValidationException("Not enough money");
        }

        Booking booking = new Booking();
        booking.setTotalPrice(seminar.getPrice());
        booking.setPromotionCode("");
        booking.setDescription(seminar.getDescription());
        booking.setRating(0);
        booking.setCreateAt(dateUtils.getCurrentVietnamDate());
        booking.setUpdateAt(dateUtils.getCurrentVietnamDate());
        booking.setStartAt(seminar.getStartTime());
        booking.setCustomer(customer);
        booking.setMeeting(meeting);
        booking.setSeminar(seminar);
        booking.setState(
                bookingStateRepository
                        .findByName("PENDING")
                        .orElseThrow(() -> new EntityNotFoundException("State not found"))
        );
        booking = bookingRepository.save(booking);

        if (booking != null) {
            seminar.setActiveSlot(seminar.getActiveSlot() - 1);
            seminarRepository.save(seminar);

            customer.getAccount().getWallet().setTokenAmount(tokenLeft);
            customerRepository.save(customer);

            Transaction transaction = new Transaction();
            transaction.setStatus(TransactionStatusEnum.SUCCESS);
            transaction.setCreateAt(dateUtils.getCurrentVietnamDate());
            transaction.setTransactionType(TransactionTypeEnum.BOOKING_SEMINAR_DONE);
            transaction.setCurrency(CurrencyEnum.TOKEN);
            transaction.setBooking(booking);
            transaction.setAmount(Double.valueOf(seminar.getPrice()));
            transaction.setWallet(customer.getAccount().getWallet());
            transactionRepository.save(transaction);
        }

        SeminarBookingDto seminarBookingDto = new SeminarBookingDto();
        seminarBookingDto.setBooking(booking);
        seminarBookingDto.setSeminar(seminar);

        return seminarBookingDto;
    }

    private ListSeminarDto mapSeminarsToDto(Page<Seminar> seminars) {
        var listSeminarDto = new ListSeminarDto();

        if (seminars == null) {
            listSeminarDto.setList(Collections.emptyList());
            listSeminarDto.setPagination(null);
            return listSeminarDto;
        } else {
            var pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(seminars.getTotalPages());
            pagingDto.setTotalOfElements(seminars.getTotalElements());
            pagingDto.setSort(seminars.getSort().toString());
            pagingDto.setCurrentPage(seminars.getNumber());
            pagingDto.setPageSize(seminars.getSize());

            listSeminarDto.setList(seminars.map(this::toSeminarDto).toList());
            listSeminarDto.setPagination(pagingDto);

            return listSeminarDto;
        }
    }

    private Pageable createPageable(Integer page, Integer pageSize, String sort) {
        if (page == null || page < 0)
            page = 0;

        if (pageSize == null || pageSize < 0)
            pageSize = 10;

        if (sort != null && sort.equals("desc")) {
            return PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        } else {
            return PageRequest.of(page, pageSize, Sort.by("createdAt").ascending());
        }
    }

    private Reader checkWorkingTime(UUID readerId, SeminarCreateDto seminarCreateDto, UUID seminarId, SeminarUpdateDto seminarUpdateDto) throws Exception {
        Reader reader = readerRepository.findById(readerId).orElseThrow(EntityNotFoundException::new);
        List<WorkingTime> workingTimes = reader.getWorkingTimes();
        List<Seminar> seminars = reader.getSeminars();

        LocalDateTime startTime;
        if (seminarCreateDto != null) {
            startTime = LocalDateTime.parse(seminarCreateDto.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            startTime = LocalDateTime.parse(seminarUpdateDto.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        int duration = (seminarCreateDto != null) ? seminarCreateDto.getDuration() : seminarUpdateDto.getDuration();

        LocalDateTime endTime = startTime.plusMinutes(duration);

        workingTimes.forEach(workingTime -> {
            LocalDateTime workingStartTime = LocalDateTime.ofInstant(workingTime.getStartTime().toInstant(), ZoneId.of("Asia/Ho_Chi_Minh"));
            LocalDateTime workingEndTime = LocalDateTime.ofInstant(workingTime.getEndTime().toInstant(), ZoneId.of("Asia/Ho_Chi_Minh"));

            if ((startTime.isAfter(workingStartTime) || startTime.isEqual(workingStartTime)) && startTime.isBefore(workingEndTime)) {
                throw new RuntimeException("Seminar start time is during working time");
            }

            if ((endTime.isAfter(workingStartTime) || endTime.isEqual(workingStartTime)) && endTime.isBefore(workingEndTime)) {
                throw new RuntimeException("Seminar end time is during working time");
            }
        });

        seminars.forEach(seminar -> {
            if (seminarId != null && seminar.getId().equals(seminarId)) return;
            LocalDateTime seminarStartTime = LocalDateTime.ofInstant(seminar.getStartTime().toInstant(), ZoneId.of("Asia/Ho_Chi_Minh"));
            LocalDateTime seminarEndTime = seminarStartTime.plusMinutes(seminar.getDuration());

            if ((startTime.isAfter(seminarStartTime) || startTime.isEqual(seminarStartTime)) && startTime.isBefore(seminarEndTime)) {
                throw new RuntimeException("Seminar start time conflicts with an existing seminar");
            }

            if ((endTime.isAfter(seminarStartTime) || endTime.isEqual(seminarStartTime)) && endTime.isBefore(seminarEndTime)) {
                throw new RuntimeException("Seminar end time conflicts with an existing seminar");
            }
        });
        return reader;
    }

    private SeminarDto toSeminarDto(Seminar seminar) {
        return SeminarDto.builder()
                .id(seminar.getId())
                .title(seminar.getTitle())
                .limitCustomer(seminar.getLimitCustomer())
                .activeSlot(seminar.getActiveSlot())
                .description(seminar.getDescription())
                .imageUrl(seminar.getImageUrl())
                .duration(seminar.getDuration())
                .price(seminar.getPrice())
                .startTime(dateFormat.format(seminar.getStartTime()))
                .status(seminar.getStatus().name())
                .createdAt(dateFormat.format(seminar.getCreatedAt()))
                .updatedAt(dateFormat.format(seminar.getUpdatedAt()))
                .reader(seminar.getReader())
                .book(seminar.getBook())
                .build();
    }
}
