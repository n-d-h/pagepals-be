package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.booking.ListBookingDto;
import com.pagepal.capstone.dtos.booking.QueryDto;
import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.entities.postgre.Customer;
import com.pagepal.capstone.repositories.postgre.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
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
    private BookingDetailRepository bookingDetailRepository;
    @MockBean
    private WorkingTimeRepository workingTimeRepository;
    @MockBean
    private MeetingRepository meetingRepository;
    @Autowired
    private BookingServiceImpl bookingServiceImpl;

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
    void canGetListBookingByCustomer4() {
        when(bookingRepository.findByCustomer(any(), any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(customerRepository.findById(any())).thenReturn(java.util.Optional.of(new Customer()));
        UUID cusId = UUID.randomUUID();
        ListBookingDto actualListBookingByCustomer = bookingServiceImpl.getListBookingByCustomer(cusId,
                new QueryDto("createdAt", 10, 3));
        assertTrue(actualListBookingByCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookingByCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookingRepository).findByCustomer(any(), any());
    }



}

