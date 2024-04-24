package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.booking.*;
import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.FirebaseMessagingService;
import com.pagepal.capstone.services.NotificationService;
import com.pagepal.capstone.services.WebhookService;
import com.pagepal.capstone.services.ZoomService;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {BookingServiceImpl.class})
@ExtendWith(SpringExtension.class)
class BookingServiceImplTest {
    @MockBean
    private BookingRepository bookingRepository;
    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private ServiceRepository serviceRepository;
    @MockBean
    private BookingStateRepository bookingStateRepository;
    @MockBean
    private WorkingTimeRepository workingTimeRepository;
    @MockBean
    private MeetingRepository meetingRepository;
    @MockBean
    private SettingRepository settingRepository;
    @MockBean
    private WalletRepository walletRepository;
    @Autowired
    private BookingServiceImpl bookingServiceImpl;
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private ReaderRepository readerRepository;
    @MockBean
    private ZoomService zoomService;
    @MockBean
    private DateUtils dateUtils;
    @MockBean
    private WebhookService webhookService;
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private FirebaseMessagingService firebaseMessagingService;

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByReader(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByReader() {
        when(bookingRepository.findAllByReaderId((UUID) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        UUID readerId = UUID.randomUUID();
        QueryDto queryDto = new QueryDto();
        ListBookingDto actualListBookingByReader = bookingServiceImpl.getListBookingByReader(readerId, queryDto);
        assertTrue(actualListBookingByReader.getList().isEmpty());
        PagingDto pagination = actualListBookingByReader.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookingRepository).findAllByReaderId((UUID) any(), (Pageable) any());
        assertEquals(10, queryDto.getPageSize().intValue());
        assertEquals(0, queryDto.getPage().intValue());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByReader(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByReader2() {
        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(new Booking());
        PageImpl<Booking> pageImpl = new PageImpl<>(bookingList);
        when(bookingRepository.findAllByReaderId((UUID) any(), (Pageable) any())).thenReturn(pageImpl);
        UUID readerId = UUID.randomUUID();
        QueryDto queryDto = new QueryDto();
        ListBookingDto actualListBookingByReader = bookingServiceImpl.getListBookingByReader(readerId, queryDto);
        assertEquals(1, actualListBookingByReader.getList().size());
        PagingDto pagination = actualListBookingByReader.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(1L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(1, pagination.getPageSize().intValue());
        verify(bookingRepository).findAllByReaderId((UUID) any(), (Pageable) any());
        assertEquals(10, queryDto.getPageSize().intValue());
        assertEquals(0, queryDto.getPage().intValue());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByReader(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByReader3() {
        when(bookingRepository.findAllByReaderId((UUID) any(), (Pageable) any())).thenReturn(null);
        UUID readerId = UUID.randomUUID();
        QueryDto queryDto = new QueryDto();
        ListBookingDto actualListBookingByReader = bookingServiceImpl.getListBookingByReader(readerId, queryDto);
        assertTrue(actualListBookingByReader.getList().isEmpty());
        assertNull(actualListBookingByReader.getPagination());
        verify(bookingRepository).findAllByReaderId((UUID) any(), (Pageable) any());
        assertEquals(10, queryDto.getPageSize().intValue());
        assertEquals(0, queryDto.getPage().intValue());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByReader(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByReader5() {
        when(bookingRepository.findAllByReaderIdAndBookingState((UUID) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookingRepository.findAllByReaderId((UUID) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        UUID readerId = UUID.randomUUID();
        ListBookingDto actualListBookingByReader = bookingServiceImpl.getListBookingByReader(readerId,
                new QueryDto("startAt", "startAt", 10, 3));
        assertTrue(actualListBookingByReader.getList().isEmpty());
        PagingDto pagination = actualListBookingByReader.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookingRepository).findAllByReaderIdAndBookingState((UUID) any(), (String) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByReader(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByReader6() {
        when(bookingRepository.findAllByReaderIdAndBookingState((UUID) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookingRepository.findAllByReaderId((UUID) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        UUID readerId = UUID.randomUUID();
        ListBookingDto actualListBookingByReader = bookingServiceImpl.getListBookingByReader(readerId,
                new QueryDto("", "startAt", 10, 3));
        assertTrue(actualListBookingByReader.getList().isEmpty());
        PagingDto pagination = actualListBookingByReader.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookingRepository).findAllByReaderId((UUID) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByReader(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByReader7() {
        when(bookingRepository.findAllByReaderIdAndBookingState((UUID) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookingRepository.findAllByReaderId((UUID) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        UUID readerId = UUID.randomUUID();
        ListBookingDto actualListBookingByReader = bookingServiceImpl.getListBookingByReader(readerId,
                new QueryDto("startAt", "desc", 10, 3));
        assertTrue(actualListBookingByReader.getList().isEmpty());
        PagingDto pagination = actualListBookingByReader.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookingRepository).findAllByReaderIdAndBookingState((UUID) any(), (String) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByReader(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByReader8() {
        when(bookingRepository.findAllByReaderIdAndBookingState((UUID) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookingRepository.findAllByReaderId((UUID) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        UUID readerId = UUID.randomUUID();
        QueryDto queryDto = new QueryDto("startAt", "startAt", -1, 3);

        ListBookingDto actualListBookingByReader = bookingServiceImpl.getListBookingByReader(readerId, queryDto);
        assertTrue(actualListBookingByReader.getList().isEmpty());
        PagingDto pagination = actualListBookingByReader.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookingRepository).findAllByReaderIdAndBookingState((UUID) any(), (String) any(), (Pageable) any());
        assertEquals(0, queryDto.getPage().intValue());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByReader(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByReader10() {
        when(bookingRepository.findAllByReaderIdAndBookingState((UUID) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookingRepository.findAllByReaderId((UUID) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        UUID readerId = UUID.randomUUID();
        QueryDto queryDto = new QueryDto("startAt", "startAt", 10, -1);

        ListBookingDto actualListBookingByReader = bookingServiceImpl.getListBookingByReader(readerId, queryDto);
        assertTrue(actualListBookingByReader.getList().isEmpty());
        PagingDto pagination = actualListBookingByReader.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookingRepository).findAllByReaderIdAndBookingState((UUID) any(), (String) any(), (Pageable) any());
        assertEquals(10, queryDto.getPageSize().intValue());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByReader(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByReader11() {
        when(bookingRepository.findAllByReaderIdAndBookingState((UUID) any(), (String) any(), (Pageable) any()))
                .thenThrow(new ValidationException("An error occurred"));
        when(bookingRepository.findAllByReaderId((UUID) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        UUID readerId = UUID.randomUUID();
        assertThrows(ValidationException.class,
                () -> bookingServiceImpl.getListBookingByReader(readerId, new QueryDto("startAt", "startAt", 10, 3)));
        verify(bookingRepository).findAllByReaderIdAndBookingState((UUID) any(), (String) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByCustomer(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByCustomer() {
        when(bookingRepository.findByCustomer(any(), any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(customerRepository.findById(any())).thenReturn(java.util.Optional.of(new Customer()));
        UUID cusId = UUID.randomUUID();
        QueryDto queryDto = new QueryDto();
        ListBookingDto actualListBookingByCustomer = bookingServiceImpl.getListBookingByCustomer(cusId, queryDto);
        assertTrue(actualListBookingByCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookingByCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookingRepository).findByCustomer(any(), any());
        assertEquals(0, queryDto.getPage().intValue());
        assertEquals(10, queryDto.getPageSize().intValue());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByCustomer(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByCustomer2() {
        when(bookingRepository.findByCustomer(any(), any())).thenReturn(null);
        when(customerRepository.findById(any())).thenReturn(java.util.Optional.of(new Customer()));
        UUID cusId = UUID.randomUUID();
        QueryDto queryDto = new QueryDto();
        ListBookingDto actualListBookingByCustomer = bookingServiceImpl.getListBookingByCustomer(cusId, queryDto);
        assertTrue(actualListBookingByCustomer.getList().isEmpty());
        assertNull(actualListBookingByCustomer.getPagination());
        verify(bookingRepository).findByCustomer(any(), any());
        assertEquals(0, queryDto.getPage().intValue());
        assertEquals(10, queryDto.getPageSize().intValue());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByCustomer(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByCustomer3() {
        when(bookingRepository.findByCustomer((Customer) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(customerRepository.findById((UUID) any())).thenReturn(Optional.of(new Customer()));
        UUID cusId = UUID.randomUUID();
        QueryDto queryDto = new QueryDto();
        ListBookingDto actualListBookingByCustomer = bookingServiceImpl.getListBookingByCustomer(cusId, queryDto);
        assertTrue(actualListBookingByCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookingByCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookingRepository).findByCustomer((Customer) any(), (Pageable) any());
        verify(customerRepository).findById((UUID) any());
        assertEquals(10, queryDto.getPageSize().intValue());
        assertEquals(0, queryDto.getPage().intValue());
    }


    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByCustomer(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByCustomer4() {
        when(bookingRepository.findByCustomer(any(), any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(customerRepository.findById(any())).thenReturn(java.util.Optional.of(new Customer()));
        UUID cusId = UUID.randomUUID();
        ListBookingDto actualListBookingByCustomer = bookingServiceImpl.getListBookingByCustomer(cusId,
                new QueryDto("", "createdAt", 10, 3));
        assertTrue(actualListBookingByCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookingByCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookingRepository).findByCustomer(any(), any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByCustomer(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByCustomer5() {
        when(bookingRepository.findByCustomer((Customer) any(), (Pageable) any()))
                .thenThrow(new ValidationException("An error occurred"));
        when(customerRepository.findById((UUID) any())).thenReturn(Optional.of(new Customer()));
        UUID cusId = UUID.randomUUID();
        assertThrows(ValidationException.class, () -> bookingServiceImpl.getListBookingByCustomer(cusId, new QueryDto()));
        verify(bookingRepository).findByCustomer((Customer) any(), (Pageable) any());
        verify(customerRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByCustomer(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByCustomer6() {
        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(new Booking());
        PageImpl<Booking> pageImpl = new PageImpl<>(bookingList);
        when(bookingRepository.findByCustomer((Customer) any(), (Pageable) any())).thenReturn(pageImpl);
        when(customerRepository.findById((UUID) any())).thenReturn(Optional.of(new Customer()));
        UUID cusId = UUID.randomUUID();
        QueryDto queryDto = new QueryDto();
        ListBookingDto actualListBookingByCustomer = bookingServiceImpl.getListBookingByCustomer(cusId, queryDto);
        assertEquals(1, actualListBookingByCustomer.getList().size());
        PagingDto pagination = actualListBookingByCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(1L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(1, pagination.getPageSize().intValue());
        verify(bookingRepository).findByCustomer((Customer) any(), (Pageable) any());
        verify(customerRepository).findById((UUID) any());
        assertEquals(10, queryDto.getPageSize().intValue());
        assertEquals(0, queryDto.getPage().intValue());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByCustomer(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByCustomer7() {
        when(bookingRepository.findByCustomer((Customer) any(), (Pageable) any())).thenReturn(null);
        when(customerRepository.findById((UUID) any())).thenReturn(Optional.of(new Customer()));
        UUID cusId = UUID.randomUUID();
        QueryDto queryDto = new QueryDto();
        ListBookingDto actualListBookingByCustomer = bookingServiceImpl.getListBookingByCustomer(cusId, queryDto);
        assertTrue(actualListBookingByCustomer.getList().isEmpty());
        assertNull(actualListBookingByCustomer.getPagination());
        verify(bookingRepository).findByCustomer((Customer) any(), (Pageable) any());
        verify(customerRepository).findById((UUID) any());
        assertEquals(10, queryDto.getPageSize().intValue());
        assertEquals(0, queryDto.getPage().intValue());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByCustomer(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByCustomer9() {
        when(bookingRepository.findByCustomer((Customer) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(customerRepository.findById((UUID) any())).thenReturn(Optional.empty());
        UUID cusId = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class,
                () -> bookingServiceImpl.getListBookingByCustomer(cusId, new QueryDto()));
        verify(customerRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByCustomer(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByCustomer11() {
        when(bookingRepository.findAllByCustomerIdAndBookingState((UUID) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookingRepository.findByCustomer((Customer) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(customerRepository.findById((UUID) any())).thenReturn(Optional.of(new Customer()));
        UUID cusId = UUID.randomUUID();
        ListBookingDto actualListBookingByCustomer = bookingServiceImpl.getListBookingByCustomer(cusId,
                new QueryDto("startAt", "startAt", 10, 3));
        assertTrue(actualListBookingByCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookingByCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookingRepository).findAllByCustomerIdAndBookingState((UUID) any(), (String) any(), (Pageable) any());
        verify(customerRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByCustomer(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByCustomer12() {
        when(bookingRepository.findAllByCustomerIdAndBookingState((UUID) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookingRepository.findByCustomer((Customer) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(customerRepository.findById((UUID) any())).thenReturn(Optional.of(new Customer()));
        UUID cusId = UUID.randomUUID();
        ListBookingDto actualListBookingByCustomer = bookingServiceImpl.getListBookingByCustomer(cusId,
                new QueryDto("", "startAt", 10, 3));
        assertTrue(actualListBookingByCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookingByCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookingRepository).findByCustomer((Customer) any(), (Pageable) any());
        verify(customerRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByCustomer(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByCustomer13() {
        when(bookingRepository.findAllByCustomerIdAndBookingState((UUID) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookingRepository.findByCustomer((Customer) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(customerRepository.findById((UUID) any())).thenReturn(Optional.of(new Customer()));
        UUID cusId = UUID.randomUUID();
        ListBookingDto actualListBookingByCustomer = bookingServiceImpl.getListBookingByCustomer(cusId,
                new QueryDto("startAt", "desc", 10, 3));
        assertTrue(actualListBookingByCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookingByCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookingRepository).findAllByCustomerIdAndBookingState((UUID) any(), (String) any(), (Pageable) any());
        verify(customerRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByCustomer(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByCustomer14() {
        when(bookingRepository.findAllByCustomerIdAndBookingState((UUID) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookingRepository.findByCustomer((Customer) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(customerRepository.findById((UUID) any())).thenReturn(Optional.of(new Customer()));
        UUID cusId = UUID.randomUUID();
        QueryDto queryDto = new QueryDto("startAt", "startAt", -1, 3);

        ListBookingDto actualListBookingByCustomer = bookingServiceImpl.getListBookingByCustomer(cusId, queryDto);
        assertTrue(actualListBookingByCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookingByCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookingRepository).findAllByCustomerIdAndBookingState((UUID) any(), (String) any(), (Pageable) any());
        verify(customerRepository).findById((UUID) any());
        assertEquals(0, queryDto.getPage().intValue());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByCustomer(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByCustomer16() {
        when(bookingRepository.findAllByCustomerIdAndBookingState((UUID) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookingRepository.findByCustomer((Customer) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(customerRepository.findById((UUID) any())).thenReturn(Optional.of(new Customer()));
        UUID cusId = UUID.randomUUID();
        QueryDto queryDto = new QueryDto("startAt", "startAt", 10, -1);

        ListBookingDto actualListBookingByCustomer = bookingServiceImpl.getListBookingByCustomer(cusId, queryDto);
        assertTrue(actualListBookingByCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookingByCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookingRepository).findAllByCustomerIdAndBookingState((UUID) any(), (String) any(), (Pageable) any());
        verify(customerRepository).findById((UUID) any());
        assertEquals(10, queryDto.getPageSize().intValue());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getListBookingByCustomer(UUID, QueryDto)}
     */
    @Test
    void canGetListBookingByCustomer17() {
        when(bookingRepository.findAllByCustomerIdAndBookingState((UUID) any(), (String) any(), (Pageable) any()))
                .thenThrow(new ValidationException("An error occurred"));
        when(bookingRepository.findByCustomer((Customer) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(customerRepository.findById((UUID) any())).thenReturn(Optional.of(new Customer()));
        UUID cusId = UUID.randomUUID();
        assertThrows(ValidationException.class,
                () -> bookingServiceImpl.getListBookingByCustomer(cusId, new QueryDto("startAt", "startAt", 10, 3)));
        verify(bookingRepository).findAllByCustomerIdAndBookingState((UUID) any(), (String) any(), (Pageable) any());
        verify(customerRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#createBooking(UUID, BookingCreateDto)}
     */
    @Test
    void canCreateBooking2() {
        when(customerRepository.findById((UUID) any())).thenReturn(Optional.of(new Customer()));
        when(serviceRepository.findById((UUID) any())).thenThrow(new ValidationException("An error occurred"));
        when(workingTimeRepository.findById((UUID) any())).thenReturn(Optional.of(new WorkingTime()));
        UUID cusId = UUID.randomUUID();
        assertThrows(ValidationException.class, () -> bookingServiceImpl.createBooking(cusId, new BookingCreateDto()));
        verify(customerRepository).findById((UUID) any());
        verify(serviceRepository).findById((UUID) any());
        verify(workingTimeRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#createBooking(UUID, BookingCreateDto)}
     */
    @Test
    void canCreateBooking4() {
        when(customerRepository.findById((UUID) any())).thenReturn(Optional.empty());
        when(serviceRepository.findById((UUID) any())).thenReturn(Optional.of(new Service()));
        when(workingTimeRepository.findById((UUID) any())).thenReturn(Optional.of(new WorkingTime()));
        UUID cusId = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class,
                () -> bookingServiceImpl.createBooking(cusId, new BookingCreateDto()));
        verify(customerRepository).findById((UUID) any());
    }


    /**
     * Method under test: {@link BookingServiceImpl#cancelBooking(UUID, String)}
     */
    @Test
    void canCancelBooking4() {
        UUID id = UUID.randomUUID();
        Date createAt = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Date updateAt = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Date deleteAt = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Date startAt = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Customer customer = new Customer();
        Meeting meeting = new Meeting();
        BookingState state = new BookingState();
        WorkingTime workingTime = new WorkingTime();
        Service service = new Service();
        Seminar seminar = new Seminar();

        Booking booking = new Booking(id, 1, "PENDING", "The characteristics of someone or something", "Just cause",
                "PENDING", 1, createAt, updateAt, deleteAt, startAt, customer, meeting, state, workingTime, service, seminar,
                new ArrayList<>());
        UUID id1 = UUID.randomUUID();
        booking.setState(new BookingState(id1, "PENDING", Status.ACTIVE, new ArrayList<>()));
        Optional<Booking> ofResult = Optional.of(booking);
        when(bookingRepository.findById((UUID) any())).thenReturn(ofResult);
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assertThrows(ValidationException.class, () -> bookingServiceImpl.cancelBooking(UUID.randomUUID(), "Just cause"));
        verify(bookingRepository).findById((UUID) any());
        verify(dateUtils).getCurrentVietnamDate();
    }

    /**
     * Method under test: {@link BookingServiceImpl#cancelBooking(UUID, String)}
     */
    @Test
    void canCancelBooking5() {
        BookingState bookingState = mock(BookingState.class);
        when(bookingState.getName()).thenReturn("Name");

        Booking booking = new Booking();
        booking.setState(bookingState);
        Optional<Booking> ofResult = Optional.of(booking);
        when(bookingRepository.findById((UUID) any())).thenReturn(ofResult);
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assertThrows(ValidationException.class, () -> bookingServiceImpl.cancelBooking(UUID.randomUUID(), "Just cause"));
        verify(bookingRepository).findById((UUID) any());
        verify(bookingState).getName();
    }

    /**
     * Method under test: {@link BookingServiceImpl#cancelBooking(UUID, String)}
     */
    @Test
    void canCancelBooking6() {
        when(bookingRepository.findById((UUID) any())).thenReturn(Optional.empty());
        BookingState bookingState = mock(BookingState.class);
        when(bookingState.getName()).thenReturn("Name");
        (new Booking()).setState(bookingState);
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assertThrows(EntityNotFoundException.class,
                () -> bookingServiceImpl.cancelBooking(UUID.randomUUID(), "Just cause"));
        verify(bookingRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#completeBooking(UUID)}
     */
    @Test
    void canCompleteBooking4() {
        BookingState bookingState = mock(BookingState.class);
        when(bookingState.getName()).thenReturn("Name");

        Booking booking = new Booking();
        booking.setState(bookingState);
        Optional<Booking> ofResult = Optional.of(booking);
        when(bookingRepository.findById((UUID) any())).thenReturn(ofResult);
        assertThrows(ValidationException.class, () -> bookingServiceImpl.completeBooking(UUID.randomUUID()));
        verify(bookingRepository).findById((UUID) any());
        verify(bookingState).getName();
    }

    /**
     * Method under test: {@link BookingServiceImpl#completeBooking(UUID)}
     */
    @Test
    void canCompleteBooking5() {
        when(bookingRepository.findById((UUID) any())).thenReturn(Optional.empty());
        BookingState bookingState = mock(BookingState.class);
        when(bookingState.getName()).thenReturn("Name");
        (new Booking()).setState(bookingState);
        assertThrows(EntityNotFoundException.class, () -> bookingServiceImpl.completeBooking(UUID.randomUUID()));
        verify(bookingRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#reviewBooking(UUID, ReviewBooking)}
     */
    @Test
    void canReviewBooking10() {
        Booking booking = new Booking();
        UUID id = UUID.randomUUID();
        booking.setState(new BookingState(id, "COMPLETE", Status.ACTIVE, new ArrayList<>()));
        Optional<Booking> ofResult = Optional.of(booking);

        Reader reader = new Reader();
        reader.setRating(5);
        UUID id1 = UUID.randomUUID();
        Date createdAt = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Book book = new Book();

        Service service = new Service(id1, 1, createdAt, "The characteristics of someone or something", 10.0d, -1, 1, 1, false,
                Status.ACTIVE, reader, book, new ServiceType());
        service.setRating(5);

        Booking booking1 = new Booking();
        booking1.setService(service);
        when(bookingRepository.save((Booking) any())).thenReturn(booking1);
        when(bookingRepository.findById((UUID) any())).thenReturn(ofResult);
        when(serviceRepository.save((Service) any())).thenReturn(new Service());
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        UUID id2 = UUID.randomUUID();

        ReviewBooking reviewBooking = new ReviewBooking();
        reviewBooking.setRating(1);
        assertThrows(ArithmeticException.class, () -> bookingServiceImpl.reviewBooking(id2, reviewBooking));
        verify(bookingRepository).save((Booking) any());
        verify(bookingRepository).findById((UUID) any());
        verify(dateUtils).getCurrentVietnamDate();
    }


    /**
     * Method under test: {@link BookingServiceImpl#reviewBooking(UUID, ReviewBooking)}
     */
    @Test
    void canReviewBooking12() {
        Booking booking = new Booking();
        UUID id = UUID.randomUUID();
        booking.setState(new BookingState(id, "COMPLETE", Status.ACTIVE, new ArrayList<>()));
        Optional<Booking> ofResult = Optional.of(booking);
        Service service = mock(Service.class);
        UUID id1 = UUID.randomUUID();
        Date createdAt = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Date updatedAt = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Date deletedAt = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        UUID readerUpdateReferenceId = UUID.randomUUID();
        Account account = new Account();
        ArrayList<WorkingTime> workingTimes = new ArrayList<>();
        ArrayList<Service> services = new ArrayList<>();
        ArrayList<Request> requests = new ArrayList<>();
        ArrayList<Meeting> meetings = new ArrayList<>();
        ArrayList<Seminar> seminars = new ArrayList<>();
        when(service.getReader()).thenReturn(new Reader(id1, "Nickname", 1, "Genre", "en", "GB",
                "https://example.org/example", "The characteristics of someone or something", 1, 1,
                "https://example.org/example", "https://example.org/example", createdAt, updatedAt, deletedAt,
                Status.ACTIVE, readerUpdateReferenceId, true, account, workingTimes, services,
                requests, meetings, seminars, new ArrayList<>()));
        when(service.getRating()).thenReturn(1);
        when(service.getTotalOfReview()).thenReturn(1);
        doNothing().when(service).setRating((Integer) any());
        doNothing().when(service).setTotalOfReview((Integer) any());
        service.setRating(5);

        Booking booking1 = new Booking();
        booking1.setService(service);
        when(bookingRepository.save((Booking) any())).thenReturn(booking1);
        when(bookingRepository.findById((UUID) any())).thenReturn(ofResult);
        when(serviceRepository.save((Service) any())).thenReturn(new Service());
        when(readerRepository.save((Reader) any())).thenReturn(new Reader());
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        UUID id2 = UUID.randomUUID();

        ReviewBooking reviewBooking = new ReviewBooking();
        reviewBooking.setRating(1);
        BookingDto actualReviewBookingResult = bookingServiceImpl.reviewBooking(id2, reviewBooking);
        assertNull(actualReviewBookingResult.getCancelReason());
        assertNull(actualReviewBookingResult.getWorkingTime());
        assertNull(actualReviewBookingResult.getUpdateAt());
        assertNull(actualReviewBookingResult.getTotalPrice());
        assertNull(actualReviewBookingResult.getState());
        assertNull(actualReviewBookingResult.getStartAt());
        assertNull(actualReviewBookingResult.getSeminar());
        assertNull(actualReviewBookingResult.getReview());
        assertNull(actualReviewBookingResult.getRating());
        assertNull(actualReviewBookingResult.getPromotionCode());
        assertNull(actualReviewBookingResult.getMeeting());
        assertNull(actualReviewBookingResult.getId());
        assertNull(actualReviewBookingResult.getDescription());
        assertNull(actualReviewBookingResult.getDeleteAt());
        assertNull(actualReviewBookingResult.getCustomer());
        assertNull(actualReviewBookingResult.getCreateAt());
        verify(bookingRepository).save((Booking) any());
        verify(bookingRepository).findById((UUID) any());
        verify(service).getReader();
        verify(service).getRating();
        verify(service, atLeast(1)).getTotalOfReview();
        verify(service, atLeast(1)).setRating((Integer) any());
        verify(service).setTotalOfReview((Integer) any());
        verify(serviceRepository).save((Service) any());
        verify(readerRepository).save((Reader) any());
        verify(dateUtils).getCurrentVietnamDate();
    }

    /**
     * Method under test: {@link BookingServiceImpl#reviewBooking(UUID, ReviewBooking)}
     */
    @Test
    void canReviewBooking13() {
        Booking booking = new Booking();
        UUID id = UUID.randomUUID();
        booking.setState(new BookingState(id, "COMPLETE", Status.ACTIVE, new ArrayList<>()));
        Optional<Booking> ofResult = Optional.of(booking);
        Service service = mock(Service.class);
        UUID id1 = UUID.randomUUID();
        Date createdAt = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Date updatedAt = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Date deletedAt = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        UUID readerUpdateReferenceId = UUID.randomUUID();
        Account account = new Account();
        ArrayList<WorkingTime> workingTimes = new ArrayList<>();
        ArrayList<Service> services = new ArrayList<>();
        ArrayList<Request> requests = new ArrayList<>();
        ArrayList<Meeting> meetings = new ArrayList<>();
        ArrayList<Seminar> seminars = new ArrayList<>();
        when(service.getReader()).thenReturn(new Reader(id1, "Nickname", 1, "Genre", "en", "GB",
                "https://example.org/example", "The characteristics of someone or something", 1, 1,
                "https://example.org/example", "https://example.org/example", createdAt, updatedAt, deletedAt,
                Status.ACTIVE, readerUpdateReferenceId, true, account,workingTimes, services,
                requests, meetings, seminars, new ArrayList<>()));
        when(service.getRating()).thenReturn(1);
        when(service.getTotalOfReview()).thenReturn(1);
        doNothing().when(service).setRating((Integer) any());
        doNothing().when(service).setTotalOfReview((Integer) any());
        service.setRating(5);

        Booking booking1 = new Booking();
        booking1.setCreateAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        booking1.setService(service);
        when(bookingRepository.save((Booking) any())).thenReturn(booking1);
        when(bookingRepository.findById((UUID) any())).thenReturn(ofResult);
        when(serviceRepository.save((Service) any())).thenReturn(new Service());
        when(readerRepository.save((Reader) any())).thenReturn(new Reader());
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        UUID id2 = UUID.randomUUID();

        ReviewBooking reviewBooking = new ReviewBooking();
        reviewBooking.setRating(1);
        BookingDto actualReviewBookingResult = bookingServiceImpl.reviewBooking(id2, reviewBooking);
        assertNull(actualReviewBookingResult.getCancelReason());
        assertNull(actualReviewBookingResult.getWorkingTime());
        assertNull(actualReviewBookingResult.getUpdateAt());
        assertNull(actualReviewBookingResult.getTotalPrice());
        assertNull(actualReviewBookingResult.getState());
        assertNull(actualReviewBookingResult.getStartAt());
        assertNull(actualReviewBookingResult.getSeminar());
        assertNull(actualReviewBookingResult.getReview());
        assertNull(actualReviewBookingResult.getRating());
        assertNull(actualReviewBookingResult.getPromotionCode());
        assertNull(actualReviewBookingResult.getMeeting());
        assertNull(actualReviewBookingResult.getId());
        assertNull(actualReviewBookingResult.getDescription());
        assertNull(actualReviewBookingResult.getDeleteAt());
        assertNull(actualReviewBookingResult.getCustomer());
        verify(bookingRepository).save((Booking) any());
        verify(bookingRepository).findById((UUID) any());
        verify(service).getReader();
        verify(service).getRating();
        verify(service, atLeast(1)).getTotalOfReview();
        verify(service, atLeast(1)).setRating((Integer) any());
        verify(service).setTotalOfReview((Integer) any());
        verify(serviceRepository).save((Service) any());
        verify(readerRepository).save((Reader) any());
        verify(dateUtils).getCurrentVietnamDate();
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingById(UUID)}
     */
    @Test
    void canGetBookingById() {
        when(bookingRepository.findById((UUID) any())).thenReturn(Optional.of(new Booking()));
        BookingDto actualBookingById = bookingServiceImpl.getBookingById(UUID.randomUUID());
        assertNull(actualBookingById.getCancelReason());
        assertNull(actualBookingById.getWorkingTime());
        assertNull(actualBookingById.getUpdateAt());
        assertNull(actualBookingById.getTotalPrice());
        assertNull(actualBookingById.getState());
        assertNull(actualBookingById.getStartAt());
        assertNull(actualBookingById.getService());
        assertNull(actualBookingById.getSeminar());
        assertNull(actualBookingById.getReview());
        assertNull(actualBookingById.getRating());
        assertNull(actualBookingById.getPromotionCode());
        assertNull(actualBookingById.getMeeting());
        assertNull(actualBookingById.getId());
        assertNull(actualBookingById.getDescription());
        assertNull(actualBookingById.getDeleteAt());
        assertNull(actualBookingById.getCustomer());
        assertNull(actualBookingById.getCreateAt());
        verify(bookingRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingById(UUID)}
     */
    @Test
    void canGetBookingById2() {
        Booking booking = new Booking();
        UUID randomUUIDResult = UUID.randomUUID();
        booking.setId(randomUUIDResult);
        Optional<Booking> ofResult = Optional.of(booking);
        when(bookingRepository.findById((UUID) any())).thenReturn(ofResult);
        BookingDto actualBookingById = bookingServiceImpl.getBookingById(UUID.randomUUID());
        assertNull(actualBookingById.getCancelReason());
        assertNull(actualBookingById.getWorkingTime());
        assertNull(actualBookingById.getUpdateAt());
        assertNull(actualBookingById.getTotalPrice());
        assertNull(actualBookingById.getState());
        assertNull(actualBookingById.getStartAt());
        assertNull(actualBookingById.getService());
        assertNull(actualBookingById.getSeminar());
        assertNull(actualBookingById.getReview());
        assertNull(actualBookingById.getRating());
        assertNull(actualBookingById.getPromotionCode());
        assertNull(actualBookingById.getMeeting());
        assertSame(randomUUIDResult, actualBookingById.getId());
        assertNull(actualBookingById.getDescription());
        assertNull(actualBookingById.getCreateAt());
        assertNull(actualBookingById.getDeleteAt());
        assertNull(actualBookingById.getCustomer());
        verify(bookingRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link BookingServiceImpl#getBookingById(UUID)}
     */
    @Test
    void canGetBookingById3() {
        Booking booking = new Booking();
        booking.setCreateAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        Optional<Booking> ofResult = Optional.of(booking);
        when(bookingRepository.findById((UUID) any())).thenReturn(ofResult);
        BookingDto actualBookingById = bookingServiceImpl.getBookingById(UUID.randomUUID());
        assertNull(actualBookingById.getCancelReason());
        assertNull(actualBookingById.getWorkingTime());
        assertNull(actualBookingById.getUpdateAt());
        assertNull(actualBookingById.getTotalPrice());
        assertNull(actualBookingById.getState());
        assertNull(actualBookingById.getStartAt());
        assertNull(actualBookingById.getService());
        assertNull(actualBookingById.getSeminar());
        assertNull(actualBookingById.getReview());
        assertNull(actualBookingById.getRating());
        assertNull(actualBookingById.getPromotionCode());
        assertNull(actualBookingById.getMeeting());
        assertNull(actualBookingById.getId());
        assertNull(actualBookingById.getDescription());
        assertNull(actualBookingById.getDeleteAt());
        assertNull(actualBookingById.getCustomer());
        verify(bookingRepository).findById((UUID) any());
    }


}

