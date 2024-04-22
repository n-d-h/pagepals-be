package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.dtos.reader.*;
import com.pagepal.capstone.dtos.request.RequestDto;
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
import com.pagepal.capstone.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
    private final DateUtils dateUtils;
    private final BookRepository bookRepository;

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
                    .filter(reader -> reader.getAccount() != null
                            && reader.getAccount().getAccountState().getName().equals(readerActive)
                    )
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

        List<Reader> readers = readerRepository.findTop8ByAccountInOrderByRatingDesc(accounts);
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
            Date now = dateUtils.getCurrentVietnamDate();
            for (WorkingTime workingTime : workingTimes) {
                if (workingTime.getStartTime().after(now) && workingTime.getBookings() == null) {
                    result.add(WorkingTimeMapper.INSTANCE.toDto(workingTime));
                } else if (workingTime.getStartTime().after(now) && workingTime.getBookings() != null) {
                    List<Booking> bookings = workingTime.getBookings().stream()
                            .filter(booking -> booking.getState().getName().equals("PENDING")).toList();
                    if (bookings == null || bookings.isEmpty())
                        result.add(WorkingTimeMapper.INSTANCE.toDto(workingTime));
                }
            }
            list = divideWorkingTimes(result);
        }
        return list;
    }

    @Override
    public ReaderBookListDto getBookOfReader(UUID id, ReaderBookFilterDto filter) {
        Reader reader = readerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reader not found"));
        List<ReaderBookDto> books = new ArrayList<>();

        if (filter.getPage() == null || filter.getPage() < 0)
            filter.setPage(0);
        if (filter.getPageSize() == null || filter.getPageSize() < 0)
            filter.setPageSize(10);

        Pageable pageable;
        pageable = PageRequest.of(filter.getPage(), filter.getPageSize());

        Page<Book> page;
        if (filter.getTitle() == null || filter.getTitle().isEmpty()) {
            page = bookRepository.findByReaderId(id, pageable);
        } else {
            page = bookRepository.findByReaderIdAndTitleContaining(id, filter.getTitle().toLowerCase(), pageable);
        }

        ReaderBookListDto listReaderDto = new ReaderBookListDto();
        if (page != null) {
            for (var book : page) {
                List<ServiceDto> services = book.getServices().stream().map(ServiceMapper.INSTANCE::toDto).collect(Collectors.toList());
                books.add(new ReaderBookDto(BookMapper.INSTANCE.toDto(book), services));
            }
            PagingDto pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(page.getTotalPages());
            pagingDto.setTotalOfElements(page.getTotalElements());
            pagingDto.setSort(page.getSort().toString());
            pagingDto.setCurrentPage(page.getNumber());
            pagingDto.setPageSize(page.getSize());

            listReaderDto.setList(books);
            listReaderDto.setPaging(pagingDto);
            return listReaderDto;
        } else {
            listReaderDto.setList(Collections.emptyList());
            listReaderDto.setPaging(null);
            return listReaderDto;
        }
    }

    @Override
    public ReaderDto registerReader(UUID accountId, RequestInputDto requestInputDto) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        Reader reader = account.getReader();
        if (reader == null) {
            reader = new Reader();
        }
        reader.setNickname(requestInputDto.getInformation().getNickname());
        reader.setGenre(requestInputDto.getInformation().getGenres().toString().replaceAll("\\[|\\]", ""));
        reader.setLanguage(requestInputDto.getInformation().getLanguages().toString().replaceAll("\\[|\\]", ""));
        reader.setAvatarUrl(requestInputDto.getInformation().getAvatarUrl());
        reader.setCountryAccent(requestInputDto.getInformation().getCountryAccent());
        reader.setDescription(requestInputDto.getInformation().getDescription());
        reader.setIntroductionVideoUrl(requestInputDto.getInformation().getIntroductionVideoUrl());
        reader.setAudioDescriptionUrl(requestInputDto.getInformation().getAudioDescriptionUrl());
        reader.setTotalOfReviews(0);
        reader.setTotalOfBookings(0);
        reader.setExperience(0.0);
        reader.setRating(0);
        reader.setStatus(Status.ACTIVE);
        reader.setAccount(account);

        reader = readerRepository.save(reader);
        if (reader != null) {
            account.setAccountState(accountStateRepository
                    .findByNameAndStatus(readerPending, Status.ACTIVE)
                    .orElseThrow(() -> new EntityNotFoundException("Account State not found")));
            accountRepository.save(account);

            Request request = new Request();
            request.setCreatedAt(dateUtils.getCurrentVietnamDate());
            request.setUpdatedAt(dateUtils.getCurrentVietnamDate());
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
        Reader isReaderUpdate = readerRepository.findByReaderUpdateReferenceId(id).orElse(null);
        if(isReaderUpdate != null) throw new RuntimeException("Reader is updating");
        reader.setIsUpdating(true);

        Reader readerUpdate = new Reader();

        readerUpdate.setNickname(readerUpdateDto.getNickname());
        readerUpdate.setGenre(readerUpdateDto.getGenres().toString().replaceAll("\\[|\\]", ""));
        readerUpdate.setLanguage(readerUpdateDto.getLanguages().toString().replaceAll("\\[|\\]", ""));
        readerUpdate.setCountryAccent(readerUpdateDto.getCountryAccent());
        readerUpdate.setDescription(readerUpdateDto.getDescription());
        readerUpdate.setIntroductionVideoUrl(readerUpdateDto.getIntroductionVideoUrl());
        readerUpdate.setAudioDescriptionUrl(readerUpdateDto.getAudioDescriptionUrl());
        readerUpdate.setAvatarUrl(readerUpdateDto.getAvatarUrl());
        readerUpdate.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        readerUpdate.setIsUpdating(true);
        readerUpdate.setReaderUpdateReferenceId(reader.getId());

        readerUpdate = readerRepository.save(readerUpdate);

        if (readerUpdate != null) {
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

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (var booking : bookings) {
            ReaderReviewDto review = new ReaderReviewDto();
            review.setRating(booking.getRating());
            review.setReview(booking.getReview());
            review.setDate(outputFormat.format(booking.getUpdateAt()));
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

    @Override
    public ReaderRequestReadDto getUpdateRequestByReaderId(UUID readerId) {

        var reader = readerRepository.findById(readerId).orElseThrow(() -> new EntityNotFoundException("Reader not found"));

        Reader clone = readerRepository.findByReaderUpdateReferenceId(readerId).orElse(null);

        if (clone != null) {
            return ReaderRequestReadDto.builder()
                    .id(clone.getId())
                    .preference(ReaderMapper.INSTANCE.toDto(reader))
                    .nickname(clone.getNickname())
                    .genres(Arrays.stream(clone.getGenre().split(",")).map(String::trim).collect(Collectors.toList()))
                    .languages(Arrays.stream(clone.getLanguage().split(",")).map(String::trim).collect(Collectors.toList()))
                    .countryAccent(clone.getCountryAccent())
                    .description(clone.getDescription())
                    .introductionVideoUrl(clone.getIntroductionVideoUrl())
                    .audioDescriptionUrl(clone.getAudioDescriptionUrl())
                    .avatarUrl(clone.getAvatarUrl())
                    .build();
        }
        return null;
    }

    @Override
    public ListReaderUpdateRequestDto getAllUpdateRequestedReader(Integer page, Integer pageSize) {
        if (page == null || page < 0)
            page = 0;
        if (pageSize == null || pageSize < 0)
            pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Reader> readers = readerRepository.findByReaderUpdateReferenceIdIsNotNullAndAccountIsNull(pageable);
        List<ReaderRequestReadDto> list = new ArrayList<>();
        for (var reader : readers.getContent()) {
            var oldReader = readerRepository.findById(reader.getReaderUpdateReferenceId()).orElse(null);
            list.add(
                    ReaderRequestReadDto.builder()
                            .id(reader.getId())
                            .preference(ReaderMapper.INSTANCE.toDto(oldReader))
                            .nickname(reader.getNickname())
                            .genres(Arrays.stream(reader.getGenre().split(",")).map(String::trim).collect(Collectors.toList()))
                            .languages(Arrays.stream(reader.getLanguage().split(",")).map(String::trim).collect(Collectors.toList()))
                            .countryAccent(reader.getCountryAccent())
                            .description(reader.getDescription())
                            .introductionVideoUrl(reader.getIntroductionVideoUrl())
                            .audioDescriptionUrl(reader.getAudioDescriptionUrl())
                            .avatarUrl(reader.getAvatarUrl())
                            .build()
            );
        }
        ListReaderUpdateRequestDto listReaderUpdateRequestDto = new ListReaderUpdateRequestDto();
        listReaderUpdateRequestDto.setList(list);
        listReaderUpdateRequestDto.setPagination(
                new PagingDto(
                        readers.getTotalPages(),
                        readers.getTotalElements(),
                        readers.getSort().toString(),
                        readers.getNumber(),
                        readers.getSize()
                )
        );
        return listReaderUpdateRequestDto;
    }

    @Override
    public ReaderDto acceptUpdateReader(UUID id) {
        Reader cloneReader = readerRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Update not found"));
        Reader reader = readerRepository
                .findById(cloneReader.getReaderUpdateReferenceId())
                .orElseThrow(() -> new EntityNotFoundException("Reader not found"));
        reader.setNickname(cloneReader.getNickname());
        reader.setGenre(cloneReader.getGenre());
        reader.setLanguage(cloneReader.getLanguage());
        reader.setCountryAccent(cloneReader.getCountryAccent());
        reader.setDescription(cloneReader.getDescription());
        reader.setIntroductionVideoUrl(cloneReader.getIntroductionVideoUrl());
        reader.setAudioDescriptionUrl(cloneReader.getAudioDescriptionUrl());
        reader.setAvatarUrl(cloneReader.getAvatarUrl());
        reader.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        reader.setIsUpdating(false);
        reader = readerRepository.save(reader);
        if (reader != null) {
            readerRepository.delete(cloneReader);
            return ReaderMapper.INSTANCE.toDto(reader);
        }
        return null;
    }

    @Override
    public ReaderDto rejectUpdateReader(UUID id) {
        Reader cloneReader = readerRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Update not found"));
        Reader reader = readerRepository
                .findById(cloneReader.getReaderUpdateReferenceId())
                .orElseThrow(() -> new EntityNotFoundException("Reader not found"));
        reader.setIsUpdating(false);
        readerRepository.delete(cloneReader);
        reader = readerRepository.save(reader);
        if (reader != null) {
            return ReaderMapper.INSTANCE.toDto(reader);
        }
        return null;
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
