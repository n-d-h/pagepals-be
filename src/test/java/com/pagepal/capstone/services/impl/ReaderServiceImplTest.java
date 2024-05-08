//package com.pagepal.capstone.services.impl;
//
//import com.pagepal.capstone.dtos.reader.ListReaderDto;
//import com.pagepal.capstone.dtos.reader.ReaderDto;
//import com.pagepal.capstone.dtos.reader.ReaderQueryDto;
//import com.pagepal.capstone.entities.postgre.*;
//import com.pagepal.capstone.enums.LoginTypeEnum;
//import com.pagepal.capstone.enums.Status;
//import com.pagepal.capstone.repositories.*;
//import com.pagepal.capstone.utils.DateUtils;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDate;
//import java.time.ZoneOffset;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ContextConfiguration(classes = {ReaderServiceImpl.class})
//@ExtendWith(SpringExtension.class)
//class ReaderServiceImplTest {
//    @MockBean
//    private AccountRepository accountRepository;
//
//    @MockBean
//    private AccountStateRepository accountStateRepository;
//
//    @MockBean
//    private ReaderRepository readerRepository;
//
//    @MockBean
//    private RoleRepository roleRepository;
//
//    @MockBean
//    private RequestRepository requestRepository;
//
//    @MockBean
//    private QuestionRepository questionRepository;
//
//    @MockBean
//    private AnswerRepository answerRepository;
//    @MockBean
//    private BookingRepository bookingRepository;
//    @MockBean
//    private DateUtils dateUtils;
//    @MockBean
//    private BookRepository bookRepository;
//    @MockBean
//    private ServiceRepository serviceRepository;
//
//    @MockBean
//    private SeminarRepository seminarRepository;
//
//    @MockBean
//    private WorkingTimeRepository workingTimeRepository;
//
//
//    @Autowired
//    private ReaderServiceImpl readerServiceImpl;
//
//
//    //Mock data
//    //Account State
//    AccountState accountState1 = new AccountState(UUID.randomUUID(), "READER_ACTIVE", Status.ACTIVE, null);
//    AccountState accountState2 = new AccountState(UUID.randomUUID(), "INACTIVE", Status.ACTIVE, null);
//
//    //Role
//    Role role1 = new Role(UUID.randomUUID(), "READER", Status.ACTIVE, null);
//    Role role2 = new Role(UUID.randomUUID(), "CUSTOMER", Status.ACTIVE, null);
//
//    //Account
//    Account account1 = new Account(UUID.randomUUID(), "username1", "password1", "email1","fullName1","0123456789", LoginTypeEnum.NORMAL,
//            new Date(), new Date(), new Date(), accountState1,null,null, null, null, role1, null, null);
//    Account account2 = new Account(UUID.randomUUID(), "username2", "password2", "email2","fullName1","0123456789", LoginTypeEnum.NORMAL,
//            new Date(), new Date(), new Date(), accountState2,null,null, null, null, role2, null, null);
//    //Reader
//    Reader reader1 = new Reader(UUID.fromString("f86cff31-9e16-4e11-8948-90c0c6fec172"), "name1", 5, "genre1", "Vietnamese", "accent1",
//            "url", "des1", 0, 0, "url","avt",
//            new Date(), new Date(), new Date(), Status.ACTIVE, null, null, account1, null,
//            null, null, null, null,null);
//    Reader reader2 = new Reader(UUID.fromString("6ff8f184-e668-4d51-ab18-89ec7d2ba014"), "name2", 5, "genre1", "Vietnamese", "accent1",
//            "url", "des1", 0, 0, "url","avt",
//            new Date(), new Date(), new Date(), Status.ACTIVE, null, null, account2,  null,
//            null, null, null, null,null);
//
//    /**
//     * Method under test: {@link ReaderServiceImpl#getReadersActive()}
//     */
//    @Test
//    void canGetReadersActive() {
//        account1.setReader(reader1);
//        account2.setReader(reader2);
//        when(accountRepository.findByAccountStateAndRole(accountState1, role1)).thenReturn(Collections.singletonList(account1));
//        when(accountStateRepository.findByNameAndStatus("READER_ACTIVE", Status.ACTIVE))
//                .thenReturn(Optional.of(accountState1));
//        when(roleRepository.findByName("READER")).thenReturn(Optional.of(role1));
//        ReaderDto readerDto = readerServiceImpl.getReadersActive().get(0);
//        assertEquals(readerDto.getNickname(), "name1");
//        verify(accountRepository).findByAccountStateAndRole(accountState1, role1);
//        verify(accountStateRepository).findByNameAndStatus("READER_ACTIVE", Status.ACTIVE);
//        verify(roleRepository).findByName("READER");
//    }
//
//    /**
//     * Method under test: {@link ReaderServiceImpl#getReadersActive()}
//     */
//    @Test
//    void shouldReturnEmptyReader() {
//        account1.setReader(reader1);
//        account2.setReader(reader2);
//        when(accountRepository.findByAccountStateAndRole(accountState1, role1)).thenReturn(new ArrayList<>());
//        when(accountStateRepository.findByNameAndStatus("READER_ACTIVE", Status.ACTIVE))
//                .thenReturn(Optional.of(accountState1));
//        when(roleRepository.findByName("READER")).thenReturn(Optional.of(role1));
//        assertTrue(readerServiceImpl.getReadersActive().isEmpty());
//        verify(accountRepository).findByAccountStateAndRole(accountState1, role1);
//        verify(accountStateRepository).findByNameAndStatus("READER_ACTIVE", Status.ACTIVE);
//        verify(roleRepository).findByName("READER");
//    }
//
//    /**
//     * Method under test: {@link ReaderServiceImpl#getReadersActive()}
//     */
//    @Test
//    void shouldThrowRunTimeExceptionWhenGetReader1() {
//        account1.setReader(reader1);
//        account2.setReader(reader2);
//
//        when(accountRepository.findByAccountStateAndRole(accountState1, role1))
//                .thenReturn(Collections.singletonList(account1));
//        when(accountStateRepository.findByNameAndStatus("ACTIVE", Status.ACTIVE))
//                .thenReturn(Optional.of(accountState1));
//        when(roleRepository.findByName("READER")).thenThrow(new EntityNotFoundException());
//
//        assertThrows(EntityNotFoundException.class, () -> {
//            readerServiceImpl.getReadersActive();
//        });
//    }
//
//    /**
//     * Method under test: {@link ReaderServiceImpl#getReadersActive()}
//     */
//    @Test
//    void shouldThrowRunTimeExceptionWhenGetReader2() {
//        account1.setReader(reader1);
//        account2.setReader(reader2);
//
//        when(accountRepository.findByAccountStateAndRole(accountState1, role1))
//                .thenReturn(Collections.singletonList(account1));
//        when(accountStateRepository.findByNameAndStatus("ACTIVE", Status.ACTIVE))
//                .thenThrow(new EntityNotFoundException());
//        when(roleRepository.findByName("READER")).thenReturn(Optional.of(role1));
//
//        assertThrows(EntityNotFoundException.class, () -> {
//            readerServiceImpl.getReadersActive();
//        });
//    }
//
//
//    /**
//     * Method under test: {@link ReaderServiceImpl#getReaderById(UUID)} ()}
//     */
//    @Test
//    void canGetReaderDetailById() {
//        account1.setReader(reader1);
//        account2.setReader(reader2);
//        when(readerRepository.findById(UUID.fromString("f86cff31-9e16-4e11-8948-90c0c6fec172")))
//                .thenReturn(Optional.of(reader1));
//        ReaderDto readerDto = readerServiceImpl.getReaderById(UUID.fromString("f86cff31-9e16-4e11-8948-90c0c6fec172"));
//        assertEquals(readerDto.getNickname(), "name1");
//        verify(readerRepository).findById(UUID.fromString("f86cff31-9e16-4e11-8948-90c0c6fec172"));
//    }
//
//    /**
//     * Method under test: {@link ReaderServiceImpl#getListReaders(ReaderQueryDto)}
//     */
//    @Test
//    void canGetListReaders() {
//        ReaderQueryDto readerQueryDto = new ReaderQueryDto();
//        readerQueryDto.setNickname("name1");
//        readerQueryDto.setGenre("genre1");
//        readerQueryDto.setLanguage("Vietnamese");
//        readerQueryDto.setCountryAccent("accent1");
//        readerQueryDto.setRating(5);
//        readerQueryDto.setSort("desc");
//        readerQueryDto.setPage(0);
//        readerQueryDto.setPageSize(10);
//
//        List<Reader> readerList = Arrays.asList(reader1, reader2);
//        Page<Reader> page = new PageImpl<>(readerList, PageRequest.of(readerQueryDto.getPage(), readerQueryDto.getPageSize()), readerList.size());
//
//        when(readerRepository.findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndRatingAndAccountState(
//                readerQueryDto.getNickname(),
//                readerQueryDto.getGenre(),
//                readerQueryDto.getLanguage(),
//                readerQueryDto.getCountryAccent(),
//                readerQueryDto.getRating(),
//                "READER_ACTIVE",
//                PageRequest.of(readerQueryDto.getPage(), readerQueryDto.getPageSize(), Sort.by("createdAt").descending())
//        )).thenReturn(page);
//
//        ListReaderDto readers = readerServiceImpl.getListReaders(readerQueryDto);
//
//        assertEquals(readers.getList().get(0).getNickname(), readerQueryDto.getNickname());
//        verify(readerRepository).findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndRatingAndAccountState(
//                readerQueryDto.getNickname(),
//                readerQueryDto.getGenre(),
//                readerQueryDto.getLanguage(),
//                readerQueryDto.getCountryAccent(),
//                readerQueryDto.getRating(),
//                "READER_ACTIVE",
//                PageRequest.of(readerQueryDto.getPage(), readerQueryDto.getPageSize(), Sort.by("createdAt").descending())
//        );
//    }
//
//
//    /**
//     * Method under test: {@link ReaderServiceImpl#getListPopularReaders()}
//     */
//    @Test
//    void canGetListPopularReaders() {
//        // Mock setup
//        when(accountStateRepository.findByNameAndStatus("READER_ACTIVE", Status.ACTIVE)).thenReturn(Optional.of(accountState1));
//        when(roleRepository.findByName("READER")).thenReturn(Optional.of(role1));
//        when(accountRepository.findByAccountStateAndRole(accountState1, role1)).thenReturn(Collections.singletonList(account1));
//        when(readerRepository.findTop8ByAccountInOrderByRatingDesc(Collections.singletonList(account1))).thenReturn(Collections.singletonList(reader1));
//
//        // Call the method under test
//        List<ReaderDto> result = readerServiceImpl.getListPopularReaders();
//
//        // Verify the interaction with the mock
//        verify(readerRepository).findTop8ByAccountInOrderByRatingDesc(Collections.singletonList(account1));
//
//        // Additional assertions if needed
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        // Add more assertions as needed
//    }
//
//}
//
