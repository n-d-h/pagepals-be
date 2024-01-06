package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.reader.ReaderDto;
import com.pagepal.capstone.dtos.reader.ReaderProfileDto;
import com.pagepal.capstone.dtos.reader.ReaderQueryDto;
import com.pagepal.capstone.dtos.reader.ReaderUpdateDto;
import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.AccountState;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.Role;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.ReaderMapper;
import com.pagepal.capstone.mappers.ServiceMapper;
import com.pagepal.capstone.repositories.postgre.AccountRepository;
import com.pagepal.capstone.repositories.postgre.AccountStateRepository;
import com.pagepal.capstone.repositories.postgre.ReaderRepository;
import com.pagepal.capstone.repositories.postgre.RoleRepository;
import com.pagepal.capstone.services.ReaderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .orElseThrow(() -> new RuntimeException("Account State not found"));
        Role role = roleRepository
                .findByName("READER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        List<Account> accounts = accountRepository.findByAccountStateAndRole(accountState, role);
        List<Reader> readers = accounts.stream().map(Account::getReader).toList();
        return readers.stream().map(ReaderMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Override
    public ReaderDto getReaderById(UUID id) {
        Reader reader = readerRepository.findById(id).orElseThrow(() -> new RuntimeException("Reader not found"));
        return ReaderMapper.INSTANCE.toDto(reader);
    }

    @Override
    public List<ReaderDto> getListReaders(ReaderQueryDto readerQueryDto) {

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

        if (page == null) return Collections.emptyList();
        else return page.map(ReaderMapper.INSTANCE::toDto).toList();

    }

    @Override
    public List<ServiceDto> getListServicesByReaderId(UUID id) {
        Reader reader = readerRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Reader not found")
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
                        new RuntimeException("Reader not found")
                );
        return ReaderMapper.INSTANCE.toProfileDto(reader);
    }

    @Override
    public ReaderProfileDto updateReaderProfile(UUID id, ReaderUpdateDto readerUpdateDto) {
        Reader reader = readerRepository.findById(id).orElseThrow(() -> new RuntimeException("Reader not found"));

        reader.setNickname(readerUpdateDto.getNickname());
        reader.setGenre(readerUpdateDto.getGenre());
        reader.setLanguage(readerUpdateDto.getLanguage());
        reader.setCountryAccent(readerUpdateDto.getCountryAccent());
        reader.setDescription(readerUpdateDto.getDescription());
        reader.setIntroductionVideoUrl(readerUpdateDto.getIntroductionVideoUrl());
        reader.setTags(readerUpdateDto.getTags());
        reader.setAudioDescriptionUrl(readerUpdateDto.getAudioDescriptionUrl());
        reader.setUpdatedAt(new Date());

        return ReaderMapper.INSTANCE.toProfileDto(readerRepository.save(reader));
    }
}
