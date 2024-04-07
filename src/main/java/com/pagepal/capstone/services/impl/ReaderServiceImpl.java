package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.dtos.reader.*;
import com.pagepal.capstone.dtos.request.RequestInputDto;
import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.dtos.workingtime.TimeSlot;
import com.pagepal.capstone.dtos.workingtime.WorkingDate;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeDto;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeListRead;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.RequestStateEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.*;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.ReaderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final AccountRepository accountRepository;
    private final AccountStateRepository accountStateRepository;
    private final RoleRepository roleRepository;
    private final RequestRepository requestRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    private final String readerActive = "READER_ACTIVE";

    private final String readerPending = "READER_PENDING";
    private final BookingRepository bookingRepository;

    @Override
    public List<ReaderDto> getReadersActive() {
        AccountState accountState = accountStateRepository
                .findByNameAndStatus(readerActive, Status.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Account State not found"));
        Role role = roleRepository
                .findByName("READER")
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        List<Account> accounts = accountRepository.findByAccountStateAndRole(accountState, role);
        List<Reader> readers = accounts.stream().map(Account::getReader).toList();
        return readers.stream().map(ReaderMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Override
    public ReaderDto getReaderById(UUID id) {
        Reader reader = readerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reader not found"));
        return ReaderMapper.INSTANCE.toDto(reader);
    }

    @Secured({"CUSTOMER", "READER", "STAFF", "ADMIN"})
    @Override
    public ListReaderDto getListReaders(ReaderQueryDto readerQueryDto) {

        if (readerQueryDto.getPage() == null || readerQueryDto.getPage() < 0)
            readerQueryDto.setPage(0);
        if (readerQueryDto.getPageSize() == null || readerQueryDto.getPageSize() < 0)
            readerQueryDto.setPageSize(10);

        Pageable pageable;
        if (readerQueryDto.getSort() != null && readerQueryDto.getSort().equals("desc")) {
            pageable = PageRequest.of(readerQueryDto.getPage(), readerQueryDto.getPageSize(), Sort.by("createdAt").descending());
        } else {
            pageable = PageRequest.of(readerQueryDto.getPage(), readerQueryDto.getPageSize(), Sort.by("createdAt").ascending());
        }

        if (readerQueryDto.getNickname() == null) readerQueryDto.setNickname("");
        if (readerQueryDto.getGenre() == null) readerQueryDto.setGenre("");
        if (readerQueryDto.getLanguage() == null) readerQueryDto.setLanguage("");
        if (readerQueryDto.getCountryAccent() == null) readerQueryDto.setCountryAccent("");

        Page<Reader> page;
        if (readerQueryDto.getRating() != null)
            page = readerRepository
                    .findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndRating(
                            readerQueryDto.getNickname(),
                            readerQueryDto.getGenre(),
                            readerQueryDto.getLanguage(),
                            readerQueryDto.getCountryAccent(),
                            readerQueryDto.getRating(),
                            pageable
                    );

        else page = readerRepository
                .findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCase(
                        readerQueryDto.getNickname(),
                        readerQueryDto.getGenre(),
                        readerQueryDto.getLanguage(),
                        readerQueryDto.getCountryAccent(),
                        pageable
                );


        ListReaderDto listReaderDto = new ListReaderDto();
        if (page == null) {
            listReaderDto.setList(Collections.emptyList());
            listReaderDto.setPagination(null);
            return listReaderDto;
        } else {
            page = new PageImpl<>(page.stream()
                    .filter(reader -> reader.getAccount().getAccountState().getName().equals(readerActive))
                    .collect(Collectors.toList()), pageable, page.getTotalElements());
            PagingDto pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(page.getTotalPages());
            pagingDto.setTotalOfElements(page.getTotalElements());
            pagingDto.setSort(page.getSort().toString());
            pagingDto.setCurrentPage(page.getNumber());
            pagingDto.setPageSize(page.getSize());

            listReaderDto.setList(page.map(ReaderMapper.INSTANCE::toDto).toList());
            listReaderDto.setPagination(pagingDto);
            return listReaderDto;
        }
    }

    @Override
    public List<ReaderDto> getListPopularReaders() {
        AccountState accountState = accountStateRepository
                .findByNameAndStatus(readerActive, Status.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Account State not found"));
        Role role = roleRepository
                .findByName("READER")
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        List<Account> accounts = accountRepository.findByAccountStateAndRole(accountState, role);

        List<Reader> readers = readerRepository.findTop10ByAccountInOrderByRatingDesc(accounts);
        return readers.stream().map(ReaderMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ServiceDto> getListServicesByReaderId(UUID id) {
        Reader reader = readerRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Reader not found")
                );
        if (reader.getServices() != null) {
            return reader
                    .getServices()
                    .stream()
                    .map(ServiceMapper.INSTANCE::toDto)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public ReaderProfileDto getReaderProfileById(UUID id) {
        Reader reader = readerRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Reader not found")
                );
        ReaderProfileDto readerProfile = new ReaderProfileDto();
        readerProfile.setProfile(ReaderMapper.INSTANCE.toDto(reader));
        readerProfile.setWorkingTimeList(getWorkingTimesAvailableByReader(id));
        return readerProfile;
    }

    @Override
    public WorkingTimeListRead getWorkingTimesAvailableByReader(UUID id) {
        Reader reader = readerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reader not found"));
        List<WorkingTime> workingTimes = reader.getWorkingTimes();
        List<WorkingTimeDto> result = new ArrayList<>();
        WorkingTimeListRead list = new WorkingTimeListRead();
        if (workingTimes != null) {
            Date now = new Date();
            result = workingTimes.stream()
                    .filter(p -> p.getDate().after(now) && p.getBooking() == null)
                    .map(WorkingTimeMapper.INSTANCE::toDto).toList();
            list = divideWorkingTimes(result);
        }
        return list;
    }

    @Override
    public List<ReaderBookDto> getBookOfReader(UUID id) {
        Reader reader = readerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reader not found"));
        List<ReaderBookDto> books = new ArrayList<>();
        var services = reader.getServices();
        if (services != null) {
            for (var service : services) {
                boolean isAdded = false;
                Book b = service.getBook();
                if (books.isEmpty()) {
                    books.add(new ReaderBookDto(BookMapper.INSTANCE.toDto(b), List.of(ServiceMapper.INSTANCE.toDto(service))));
                } else {
                    for (var book : books) {
                        if (book.getBook().getId().equals(b.getId())) {
                            List<ServiceDto> serviceDtos = new ArrayList<>(book.getServices());
                            serviceDtos.add(ServiceMapper.INSTANCE.toDto(service));
                            book.setServices(serviceDtos);
                            isAdded = true;
                        }
                    }
                    if (!isAdded) {
                        books.add(new ReaderBookDto(BookMapper.INSTANCE.toDto(b), List.of(ServiceMapper.INSTANCE.toDto(service))));
                    }
                }
            }
        }
        return books;
    }

    @Override
    public ReaderDto registerReader(UUID accountId, RequestInputDto requestInputDto) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        Reader reader = new Reader();
        reader.setNickname(requestInputDto.getInformation().getNickname());
        reader.setGenre(requestInputDto.getInformation().getGenres().toString().replaceAll("\\[|\\]", ""));
        reader.setLanguage(requestInputDto.getInformation().getLanguages().toString().replaceAll("\\[|\\]", ""));
        reader.setAvatarUrl(requestInputDto.getInformation().getAvatarUrl());
        reader.setCountryAccent(requestInputDto.getInformation().getCountryAscent());
        reader.setDescription(requestInputDto.getInformation().getDescription());
        reader.setIntroductionVideoUrl(requestInputDto.getInformation().getIntroductionVideoUrl());
        reader.setAudioDescriptionUrl(requestInputDto.getInformation().getAudioDescriptionUrl());
        reader.setStatus(Status.ACTIVE);
        reader.setAccount(account);

        reader = readerRepository.save(reader);
        if (reader != null) {
            account.setAccountState(accountStateRepository
                    .findByNameAndStatus(readerPending, Status.ACTIVE)
                    .orElseThrow(() -> new EntityNotFoundException("Account State not found")));
            accountRepository.save(account);

            Request request = new Request();
            request.setCreatedAt(new Date());
            request.setUpdatedAt(new Date());
            request.setReader(reader);
            request.setState(RequestStateEnum.ANSWER_CHECKING);
            request = requestRepository.save(request);

            for (var answer : requestInputDto.getAnswers()) {
                Question question = questionRepository.findById(answer.getQuestionId())
                        .orElseThrow(() -> new EntityNotFoundException("Question not found"));
                Answer a = new Answer();
                a.setQuestion(question);
                a.setContent(answer.getContent());
                a.setStatus(Status.ACTIVE);
                a.setRequest(request);
                answerRepository.save(a);
            }
            return ReaderMapper.INSTANCE.toDto(reader);
        }

        return null;
    }

    @Secured({"READER", "STAFF", "ADMIN"})
    @Override
    public String updateReaderProfile(UUID id, ReaderRequestInputDto readerUpdateDto) {
        Reader reader = readerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reader not found"));
        reader.setIsUpdating(true);

        Reader readerUpdate = new Reader();

        readerUpdate.setNickname(readerUpdateDto.getNickname());
        reader.setGenre(readerUpdateDto.getGenres().toString().replaceAll("\\[|\\]", ""));
        reader.setLanguage(readerUpdateDto.getLanguages().toString().replaceAll("\\[|\\]", ""));
        readerUpdate.setCountryAccent(readerUpdateDto.getCountryAscent());
        readerUpdate.setDescription(readerUpdateDto.getDescription());
        readerUpdate.setIntroductionVideoUrl(readerUpdateDto.getIntroductionVideoUrl());
        readerUpdate.setAudioDescriptionUrl(readerUpdateDto.getAudioDescriptionUrl());
        readerUpdate.setAvatarUrl(readerUpdateDto.getAvatarUrl());
        readerUpdate.setUpdatedAt(new Date());
        readerUpdate.setIsUpdating(true);
        readerUpdate.setReaderUpdateReferenceId(reader.getId());

        readerUpdate = readerRepository.save(readerUpdate);

        if(readerUpdate != null) {
            reader = readerRepository.save(reader);
            return reader != null ? "Request update success!" : "Fail!";
        }

        return "Fail!";
    }

    @Override
    public ListReaderReviewDto getReaderReviewsByReaderId(UUID id, Integer page, Integer size) {

        if (page == null || page < 0)
            page = 0;
        if (size == null || size < 0)
            size = 10;

        Pageable pageable;
        pageable = PageRequest.of(page, size, Sort.by("updateAt").descending());

        Page<Booking> bookings = bookingRepository.findByRatingIsNotNullAndStateString("COMPLETE", id, pageable);

        List<ReaderReviewDto> reviews = new ArrayList<>();

        for (var booking : bookings) {
            ReaderReviewDto review = new ReaderReviewDto();
            review.setRating(booking.getRating());
            review.setReview(booking.getReview());
            review.setDate(booking.getUpdateAt());
            review.setCustomer(CustomerMapper.INSTANCE.toDto(booking.getCustomer()));
            review.setService(ServiceMapper.INSTANCE.toDto(booking.getService()));
            reviews.add(review);
        }

        ListReaderReviewDto list = new ListReaderReviewDto();
        if (reviews == null || reviews.isEmpty()) {
            list.setList(Collections.emptyList());
            list.setPagination(null);
            return null;
        } else {
            PagingDto pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(bookings.getTotalPages());
            pagingDto.setTotalOfElements(bookings.getTotalElements());
            pagingDto.setSort(bookings.getSort().toString());
            pagingDto.setCurrentPage(bookings.getNumber());
            pagingDto.setPageSize(bookings.getSize());

            list.setList(reviews);
            list.setPagination(pagingDto);
            return list;
        }
    }

    private static WorkingTimeListRead divideWorkingTimes(List<WorkingTimeDto> workingTimes) {
        // Group the working times by date
        Map<Date, List<WorkingTimeDto>> groupedWorkingTimes = workingTimes.stream()
                .collect(Collectors.groupingBy(WorkingTimeDto::getDate));

        // Create WorkingTimeListRead object
        WorkingTimeListRead workingTimeListRead = new WorkingTimeListRead();
        workingTimeListRead.setWorkingDates(new ArrayList<>());

        // Iterate over the grouped working times and create WorkingDate objects
        for (Map.Entry<Date, List<WorkingTimeDto>> entry : groupedWorkingTimes.entrySet()) {
            Date date = entry.getKey();
            List<WorkingTimeDto> workingTimesForDate = entry.getValue();

            // Create WorkingDate object
            WorkingDate workingDate = new WorkingDate();
            workingDate.setDate(date);
            workingDate.setTimeSlots(new ArrayList<>());

            // Iterate over the working times for the date and create TimeSlot objects
            for (WorkingTimeDto workingTimeDto : workingTimesForDate) {
                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setId(workingTimeDto.getId());
                timeSlot.setStartTime(getStartTime(workingTimeDto.getStartTime()));
                workingDate.getTimeSlots().add(timeSlot);
            }

            // Add WorkingDate object to WorkingTimeListRead
            workingTimeListRead.getWorkingDates().add(workingDate);
        }

        return workingTimeListRead;
    }

    private static String getStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        return String.format("%02d:%02d:%02d", hour, minute, second); // Example start time
    }
}
