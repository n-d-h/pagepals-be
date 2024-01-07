package com.pagepal.capstone.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pagepal.capstone.dtos.reader.*;
import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.AccountState;
import com.pagepal.capstone.entities.postgre.Follow;
import com.pagepal.capstone.entities.postgre.Level;
import com.pagepal.capstone.entities.postgre.Promotion;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.Request;
import com.pagepal.capstone.entities.postgre.Role;
import com.pagepal.capstone.entities.postgre.Service;
import com.pagepal.capstone.entities.postgre.WorkingTime;
import com.pagepal.capstone.enums.LoginTypeEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.postgre.AccountRepository;
import com.pagepal.capstone.repositories.postgre.AccountStateRepository;
import com.pagepal.capstone.repositories.postgre.ReaderRepository;
import com.pagepal.capstone.repositories.postgre.RoleRepository;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ReaderServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ReaderServiceImplTest {
    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountStateRepository accountStateRepository;

    @MockBean
    private ReaderRepository readerRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private ReaderServiceImpl readerServiceImpl;

    //Mock data
    //Account State
    AccountState accountState1 = new AccountState(UUID.randomUUID(), "ACTIVE", Status.ACTIVE, null);
    AccountState accountState2 = new AccountState(UUID.randomUUID(), "INACTIVE", Status.ACTIVE, null);

    //Role
    Role role1 = new Role(UUID.randomUUID(), "READER", Status.ACTIVE, null);
    Role role2 = new Role(UUID.randomUUID(), "CUSTOMER", Status.ACTIVE, null);

    //Account
    Account account1 = new Account(UUID.randomUUID(), "username1", "password1", "email1", LoginTypeEnum.NORMAL,
            new Date(), new Date(), new Date(), accountState1, null, null, role1, null);
    Account account2 = new Account(UUID.randomUUID(), "username2", "password2", "email2", LoginTypeEnum.NORMAL,
            new Date(), new Date(), new Date(), accountState2, null, null, role2, null);
    //Reader
    Reader reader1 = new Reader(UUID.fromString("f86cff31-9e16-4e11-8948-90c0c6fec172"), "name1", 5, "genre1", "Vietnamese", "accent1",
            "url", "des1", "123", "123", "url", 123.2, "tag",
            new Date(), new Date(), new Date(), null, account1, null, null, null, null,
            null, null, null);
    Reader reader2 = new Reader(UUID.fromString("6ff8f184-e668-4d51-ab18-89ec7d2ba014"), "name2", 5, "genre1", "Vietnamese", "accent1",
            "url", "des1", "123", "123", "url", 123.2, "tag",
            new Date(), new Date(), new Date(), null, account2, null, null, null, null,
            null, null, null);

    /**
     * Method under test: {@link ReaderServiceImpl#getReadersActive()}
     */
    @Test
    void canGetReadersActive() {
        account1.setReader(reader1);
        account2.setReader(reader2);
        when(accountRepository.findByAccountStateAndRole(accountState1, role1)).thenReturn(Arrays.asList(account1));
        when(accountStateRepository.findByNameAndStatus("ACTIVE", Status.ACTIVE))
                .thenReturn(Optional.of(accountState1));
        when(roleRepository.findByName("READER")).thenReturn(Optional.of(role1));
        ReaderDto readerDto = readerServiceImpl.getReadersActive().get(0);
        assertEquals(readerDto.getNickname(), "name1");
        verify(accountRepository).findByAccountStateAndRole(accountState1, role1);
        verify(accountStateRepository).findByNameAndStatus("ACTIVE", Status.ACTIVE);
        verify(roleRepository).findByName("READER");
    }

    /**
     * Method under test: {@link ReaderServiceImpl#getReadersActive()}
     */
    @Test
    void shouldReturnEmptyReader() {
        account1.setReader(reader1);
        account2.setReader(reader2);
        when(accountRepository.findByAccountStateAndRole(accountState1, role1)).thenReturn(new ArrayList<>());
        when(accountStateRepository.findByNameAndStatus("ACTIVE", Status.ACTIVE))
                .thenReturn(Optional.of(accountState1));
        when(roleRepository.findByName("READER")).thenReturn(Optional.of(role1));
        assertTrue(readerServiceImpl.getReadersActive().isEmpty());
        verify(accountRepository).findByAccountStateAndRole(accountState1, role1);
        verify(accountStateRepository).findByNameAndStatus("ACTIVE", Status.ACTIVE);
        verify(roleRepository).findByName("READER");
    }

    /**
     * Method under test: {@link ReaderServiceImpl#getReadersActive()}
     */
    @Test
    void shouldThrowRunTimeExceptionWhenGetReader1() {
        account1.setReader(reader1);
        account2.setReader(reader2);

        when(accountRepository.findByAccountStateAndRole(accountState1, role1))
                .thenReturn(Arrays.asList(account1));
        when(accountStateRepository.findByNameAndStatus("ACTIVE", Status.ACTIVE))
                .thenReturn(Optional.of(accountState1));
        when(roleRepository.findByName("READER")).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> {
            readerServiceImpl.getReadersActive();
        });
    }

    /**
     * Method under test: {@link ReaderServiceImpl#getReadersActive()}
     */
    @Test
    void shouldThrowRunTimeExceptionWhenGetReader2() {
        account1.setReader(reader1);
        account2.setReader(reader2);

        when(accountRepository.findByAccountStateAndRole(accountState1, role1))
                .thenReturn(Arrays.asList(account1));
        when(accountStateRepository.findByNameAndStatus("ACTIVE", Status.ACTIVE))
                .thenThrow(new RuntimeException());
        when(roleRepository.findByName("READER")).thenReturn(Optional.of(role1));

        assertThrows(RuntimeException.class, () -> {
            readerServiceImpl.getReadersActive();
        });
    }


    /**
     * Method under test: {@link ReaderServiceImpl#getReaderById(UUID)} ()}
     */
    @Test
    void canGetReaderDetailById() {
        account1.setReader(reader1);
        account2.setReader(reader2);
        when(readerRepository.findById(UUID.fromString("f86cff31-9e16-4e11-8948-90c0c6fec172")))
                .thenReturn(Optional.of(reader1));
        ReaderDto readerDto = readerServiceImpl.getReaderById(UUID.fromString("f86cff31-9e16-4e11-8948-90c0c6fec172"));
        assertEquals(readerDto.getNickname(), "name1");
        verify(readerRepository).findById(UUID.fromString("f86cff31-9e16-4e11-8948-90c0c6fec172"));
    }

    /**
     * Method under test: {@link ReaderServiceImpl#getListReaders(ReaderQueryDto)}
     */
    @Test
    void canGetListReaders() {
        ReaderQueryDto readerQueryDto = new ReaderQueryDto();
        readerQueryDto.setNickname("name1");
        readerQueryDto.setGenre("genre1");
        readerQueryDto.setLanguage("Vietnamese");
        readerQueryDto.setCountryAccent("accent1");
        readerQueryDto.setRating(5);
        readerQueryDto.setSort("desc");
        readerQueryDto.setPage(0);
        readerQueryDto.setPageSize(10);

        List<Reader> readerList = Arrays.asList(reader1, reader2);
        Page<Reader> page = new PageImpl<>(readerList, PageRequest.of(readerQueryDto.getPage(), readerQueryDto.getPageSize()), readerList.size());

        when(readerRepository.findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndRating(
                readerQueryDto.getNickname(),
                readerQueryDto.getGenre(),
                readerQueryDto.getLanguage(),
                readerQueryDto.getCountryAccent(),
                readerQueryDto.getRating(),
                PageRequest.of(readerQueryDto.getPage(), readerQueryDto.getPageSize(), Sort.by("createdAt").descending())
        )).thenReturn(page);

        ListReaderDto readers = readerServiceImpl.getListReaders(readerQueryDto);

        assertEquals(readers.getList().get(0).getNickname(), readerQueryDto.getNickname());
        verify(readerRepository).findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndRating(
                readerQueryDto.getNickname(),
                readerQueryDto.getGenre(),
                readerQueryDto.getLanguage(),
                readerQueryDto.getCountryAccent(),
                readerQueryDto.getRating(),
                PageRequest.of(readerQueryDto.getPage(), readerQueryDto.getPageSize(), Sort.by("createdAt").descending())
        );
    }

    /**
     * Method under test: {@link ReaderServiceImpl#getListServicesByReaderId(UUID)}
     */
    @Test
    void canGetListServicesByReaderId() {
        when(readerRepository.findById((UUID) any())).thenReturn(Optional.of(new Reader()));
        assertNull(readerServiceImpl.getListServicesByReaderId(UUID.randomUUID()));
        verify(readerRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link ReaderServiceImpl#getListServicesByReaderId(UUID)}
     */
    @Test
    void canGetListServicesByReaderId2() {
        UUID id = UUID.randomUUID();
        Date createdAt = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Date updatedAt = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Date deletedAt = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Account account = new Account();
        Level level = new Level();
        ArrayList<WorkingTime> workingTimes = new ArrayList<>();
        ArrayList<Service> services = new ArrayList<>();
        ArrayList<Follow> follows = new ArrayList<>();
        ArrayList<Promotion> promotions = new ArrayList<>();
        ArrayList<Request> requests = new ArrayList<>();
        when(readerRepository.findById((UUID) any())).thenReturn(Optional.of(new Reader(id, "Nickname", 1, "Genre", "en",
                "GB", "https://example.org/example", "The characteristics of someone or something", "Total Of Reviews",
                "Total Of Bookings", "https://example.org/example", 10.0d, "Tags", createdAt, updatedAt, deletedAt,
                Status.ACTIVE, account, level, workingTimes, services, follows, promotions, requests, new ArrayList<>())));
        assertTrue(readerServiceImpl.getListServicesByReaderId(UUID.randomUUID()).isEmpty());
        verify(readerRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link ReaderServiceImpl#getListServicesByReaderId(UUID)}
     */
    @Test
    void canGetListServicesByReaderId3() {
        Reader reader = mock(Reader.class);
        when(reader.getServices()).thenReturn(new ArrayList<>());
        Optional<Reader> ofResult = Optional.of(reader);
        when(readerRepository.findById((UUID) any())).thenReturn(ofResult);
        assertTrue(readerServiceImpl.getListServicesByReaderId(UUID.randomUUID()).isEmpty());
        verify(readerRepository).findById((UUID) any());
        verify(reader, atLeast(1)).getServices();
    }

    /**
     * Method under test: {@link ReaderServiceImpl#updateReaderProfile(UUID, ReaderUpdateDto)}
     */
    @Test
    void canUpdateReaderProfile() {
        when(readerRepository.save((Reader) any())).thenReturn(new Reader());
        when(readerRepository.findById((UUID) any())).thenReturn(Optional.of(new Reader()));
        UUID id = UUID.randomUUID();
        ReaderProfileDto actualUpdateReaderProfileResult = readerServiceImpl.updateReaderProfile(id,
                new ReaderUpdateDto("Nickname", "Genre", "en", "GB", "https://example.org/example",
                        "The characteristics of someone or something", "https://example.org/example", "Tags"));
        assertNull(actualUpdateReaderProfileResult.getAccount());
        assertNull(actualUpdateReaderProfileResult.getTotalOfReviews());
        assertNull(actualUpdateReaderProfileResult.getTotalOfBookings());
        assertNull(actualUpdateReaderProfileResult.getTags());
        assertEquals(Status.ACTIVE, actualUpdateReaderProfileResult.getStatus());
        assertEquals(0, actualUpdateReaderProfileResult.getRating());
        assertNull(actualUpdateReaderProfileResult.getNickname());
        assertNull(actualUpdateReaderProfileResult.getLevel());
        assertNull(actualUpdateReaderProfileResult.getLanguage());
        assertNull(actualUpdateReaderProfileResult.getIntroductionVideoUrl());
        assertNull(actualUpdateReaderProfileResult.getId());
        assertNull(actualUpdateReaderProfileResult.getGenre());
        assertNull(actualUpdateReaderProfileResult.getExperience());
        assertNull(actualUpdateReaderProfileResult.getDescription());
        assertNull(actualUpdateReaderProfileResult.getDeletedAt());
        assertNull(actualUpdateReaderProfileResult.getCountryAccent());
        assertNull(actualUpdateReaderProfileResult.getAudioDescriptionUrl());
        verify(readerRepository).save((Reader) any());
        verify(readerRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link ReaderServiceImpl#updateReaderProfile(UUID, ReaderUpdateDto)}
     */
    @Test
    void shouldThrowWhenCannotSave() {
        when(readerRepository.save((Reader) any())).thenThrow(new RuntimeException());
        when(readerRepository.findById((UUID) any())).thenReturn(Optional.of(new Reader()));
        UUID id = UUID.randomUUID();
        assertThrows(RuntimeException.class,
                () -> readerServiceImpl.updateReaderProfile(id,
                        new ReaderUpdateDto("Nickname", "Genre", "en", "GB", "https://example.org/example",
                                "The characteristics of someone or something", "https://example.org/example", "Tags")));
        verify(readerRepository).save((Reader) any());
        verify(readerRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link ReaderServiceImpl#updateReaderProfile(UUID, ReaderUpdateDto)}
     */
    @Test
    void showThrowWhenCannotFindReader() {
        when(readerRepository.save((Reader) any())).thenReturn(mock(Reader.class));
        when(readerRepository.findById((UUID) any())).thenReturn(Optional.empty());

        UUID id = UUID.randomUUID();
        assertThrows(RuntimeException.class,
                () -> readerServiceImpl.updateReaderProfile(id,
                        new ReaderUpdateDto("Nickname", "Genre", "en", "GB", "https://example.org/example",
                                "The characteristics of someone or something", "https://example.org/example", "Tags")));
        verify(readerRepository).findById((UUID) any());
    }

}

