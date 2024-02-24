package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.book.BookDto;
import com.pagepal.capstone.dtos.chapter.ChapterDto;
import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.dtos.reader.*;
import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.dtos.workingtime.TimeSlot;
import com.pagepal.capstone.dtos.workingtime.WorkingDate;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeDto;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeListRead;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.*;
import com.pagepal.capstone.repositories.postgre.AccountRepository;
import com.pagepal.capstone.repositories.postgre.AccountStateRepository;
import com.pagepal.capstone.repositories.postgre.ReaderRepository;
import com.pagepal.capstone.repositories.postgre.RoleRepository;
import com.pagepal.capstone.services.ReaderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public List<ReaderDto> getReadersActive() {
        AccountState accountState = accountStateRepository
                .findByNameAndStatus("ACTIVE", Status.ACTIVE)
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

    @Secured({"READER", "STAFF", "ADMIN"})
    @Override
    public ReaderProfileDto updateReaderProfile(UUID id, ReaderUpdateDto readerUpdateDto) {
        Reader reader = readerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reader not found"));

        reader.setNickname(readerUpdateDto.getNickname());
        reader.setGenre(readerUpdateDto.getGenre());
        reader.setLanguage(readerUpdateDto.getLanguage());
        reader.setCountryAccent(readerUpdateDto.getCountryAccent());
        reader.setDescription(readerUpdateDto.getDescription());
        reader.setIntroductionVideoUrl(readerUpdateDto.getIntroductionVideoUrl());
        reader.setTags(readerUpdateDto.getTags());
        reader.setAudioDescriptionUrl(readerUpdateDto.getAudioDescriptionUrl());
        reader.setUpdatedAt(new Date());
        Reader result = readerRepository.save(reader);
        return getReaderProfileById(result.getId());
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
                    .filter(p -> p.getDate().after(now))
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
                Book b = service.getChapter().getBook();
                if(books.isEmpty()){
                    books.add(new ReaderBookDto(BookMapper.INSTANCE.toDto(b), List.of(ChapterMapper.INSTANCE.toDto(service.getChapter()))) );
                }else{
                    for(var book: books){
                        if(book.getBook().getId().equals(b.getId())){
                            List<ChapterDto> chapters = new ArrayList<>(book.getChapters());
                            chapters.add(ChapterMapper.INSTANCE.toDto(service.getChapter()));
                            book.setChapters(chapters);
                            isAdded = true;
                        }
                    }
                    if(!isAdded){
                        books.add(new ReaderBookDto(BookMapper.INSTANCE.toDto(b), List.of(ChapterMapper.INSTANCE.toDto(service.getChapter()))) );
                    }
                }
            }
        }
        return books;
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
