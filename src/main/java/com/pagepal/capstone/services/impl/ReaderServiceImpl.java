package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.reader.ReaderDto;
import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.AccountState;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.Role;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.ReaderMapper;
import com.pagepal.capstone.repositories.postgre.AccountRepository;
import com.pagepal.capstone.repositories.postgre.AccountStateRepository;
import com.pagepal.capstone.repositories.postgre.ReaderRepository;
import com.pagepal.capstone.repositories.postgre.RoleRepository;
import com.pagepal.capstone.services.ReaderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
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
}
