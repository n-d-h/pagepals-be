package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.analytic.*;
import com.pagepal.capstone.entities.postgre.Setting;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.enums.TransactionStatusEnum;
import com.pagepal.capstone.enums.TransactionTypeEnum;
import com.pagepal.capstone.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {AnalyticServiceImpl.class})
@ExtendWith(SpringExtension.class)
class AnalyticServiceImplTest {
    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private AnalyticServiceImpl analyticServiceImpl;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private ServiceRepository serviceRepository;

    @MockBean
    private SettingRepository settingRepository;

    @MockBean
    private TransactionRepository transactionRepository;


//    /**
//     * Method under test: {@link AnalyticServiceImpl#getAnalyticAdminByDate(String, String)}
//     */
//    @Test
//    void canGetAnalyticAdminByDate5() {
//        when(accountRepository.findByRoleStringAndAccountStateString((List<String>) any(), (List<String>) any()))
//                .thenReturn(new ArrayList<>());
//        when(serviceRepository.countByStatus((Status) any())).thenReturn(3L);
//        when(bookingRepository.findByStateString((String) any())).thenReturn(new ArrayList<>());
//        when(bookingRepository.findAll()).thenReturn(new ArrayList<>());
//        when(bookingRepository.count()).thenReturn(3L);
//        when(transactionRepository.findByTransactionTypeAndStatus((TransactionTypeEnum) any(),
//                (TransactionStatusEnum) any())).thenReturn(new ArrayList<>());
//        when(settingRepository.findByKey((String) any()))
//                .thenReturn(Optional.of(new Setting(UUID.randomUUID(), "READER", "42")));
//        AnalyticAdmin actualAnalyticAdminByDate = analyticServiceImpl.getAnalyticAdminByDate("2020-03-01", "2020-03-01");
//        assertEquals(3L, actualAnalyticAdminByDate.getTotalService());
//        assertEquals(3L, actualAnalyticAdminByDate.getTotalBookings());
//        assertEquals(0L, actualAnalyticAdminByDate.getTotalCustomers());
//        assertEquals(0L, actualAnalyticAdminByDate.getTotalReaders());
//        IncomeByToken incomeByToken = actualAnalyticAdminByDate.getIncomeByToken();
//        assertEquals(0, incomeByToken.getTotalTokenSale());
//        assertEquals(0.0f, incomeByToken.getTotalIncome());
//        List<IncomeByTokenData> seriesData = incomeByToken.getSeriesData();
//        assertEquals(1, seriesData.size());
//        assertEquals(0.0f, incomeByToken.getPercentageIncrease());
//        IncomeByRevenueShare incomeByRevenueShare = actualAnalyticAdminByDate.getIncomeByRevenueShare();
//        assertEquals(0.0f, incomeByRevenueShare.getTotalIncome());
//        BookingStatics bookingStatics = actualAnalyticAdminByDate.getBookingStatics();
//        List<String> months = bookingStatics.getMonths();
//        assertEquals(1, months.size());
//        assertEquals("03/01/2020", months.get(0));
//        List<IncomeByRevenueShareData> seriesData1 = incomeByRevenueShare.getSeriesData();
//        assertEquals(1, seriesData1.size());
//        assertEquals(0, bookingStatics.getPercentageOfDone());
//        List<StateStatic> seriesData2 = bookingStatics.getSeriesData();
//        assertEquals(3, seriesData2.size());
//        assertEquals(0.0f, incomeByRevenueShare.getPercentageIncrease());
//        StateStatic getResult = seriesData2.get(0);
//        assertEquals("COMPLETE", getResult.getState());
//        IncomeByTokenData getResult1 = seriesData.get(0);
//        assertEquals(0, getResult1.getToken());
//        assertEquals(0.0f, getResult1.getIncome());
//        assertEquals("03/01/2020", getResult1.getDate());
//        List<Integer> data = getResult.getData();
//        assertEquals(1, data.size());
//        assertEquals(0, data.get(0));
//        IncomeByRevenueShareData getResult2 = seriesData1.get(0);
//        assertEquals("03/01/2020", getResult2.getDate());
//        StateStatic getResult3 = seriesData2.get(1);
//        List<Integer> data1 = getResult3.getData();
//        assertEquals(data, data1);
//        assertEquals(1, data1.size());
//        assertEquals(0, data1.get(0));
//        StateStatic getResult4 = seriesData2.get(2);
//        List<Integer> data2 = getResult4.getData();
//        assertEquals(data1, data2);
//        assertEquals(1, data2.size());
//        assertEquals(0, data2.get(0));
//        assertEquals("PENDING", getResult4.getState());
//        assertEquals(0.0f, getResult2.getIncome());
//        assertEquals("CANCEL", getResult3.getState());
//        verify(accountRepository).findByRoleStringAndAccountStateString((List<String>) any(), (List<String>) any());
//        verify(serviceRepository).countByStatus((Status) any());
//        verify(bookingRepository).findByStateString((String) any());
//        verify(bookingRepository).findAll();
//        verify(bookingRepository).count();
//        verify(transactionRepository).findByTransactionTypeAndStatus((TransactionTypeEnum) any(),
//                (TransactionStatusEnum) any());
//        verify(settingRepository, atLeast(1)).findByKey((String) any());
//    }
//
//    /**
//     * Method under test: {@link AnalyticServiceImpl#getAnalyticAdminByDate(String, String)}
//     */
//    @Test
//    void canGetAnalyticAdminByDate7() {
//        when(accountRepository.findByRoleStringAndAccountStateString((List<String>) any(), (List<String>) any()))
//                .thenReturn(new ArrayList<>());
//        when(serviceRepository.countByStatus((Status) any())).thenReturn(3L);
//        when(bookingRepository.findByStateString((String) any())).thenReturn(new ArrayList<>());
//        when(bookingRepository.findAll()).thenReturn(new ArrayList<>());
//        when(bookingRepository.count()).thenReturn(3L);
//        when(transactionRepository.findByTransactionTypeAndStatus((TransactionTypeEnum) any(),
//                (TransactionStatusEnum) any())).thenReturn(new ArrayList<>());
//        when(settingRepository.findByKey((String) any())).thenReturn(Optional.empty());
//        AnalyticAdmin actualAnalyticAdminByDate = analyticServiceImpl.getAnalyticAdminByDate("2020-03-01", "2020-03-01");
//        assertEquals(3L, actualAnalyticAdminByDate.getTotalService());
//        assertEquals(3L, actualAnalyticAdminByDate.getTotalBookings());
//        assertEquals(0L, actualAnalyticAdminByDate.getTotalCustomers());
//        assertEquals(0L, actualAnalyticAdminByDate.getTotalReaders());
//        IncomeByToken incomeByToken = actualAnalyticAdminByDate.getIncomeByToken();
//        assertEquals(0, incomeByToken.getTotalTokenSale());
//        assertEquals(0.0f, incomeByToken.getTotalIncome());
//        List<IncomeByTokenData> seriesData = incomeByToken.getSeriesData();
//        assertEquals(1, seriesData.size());
//        assertEquals(0.0f, incomeByToken.getPercentageIncrease());
//        IncomeByRevenueShare incomeByRevenueShare = actualAnalyticAdminByDate.getIncomeByRevenueShare();
//        assertEquals(0.0f, incomeByRevenueShare.getTotalIncome());
//        BookingStatics bookingStatics = actualAnalyticAdminByDate.getBookingStatics();
//        List<String> months = bookingStatics.getMonths();
//        assertEquals(1, months.size());
//        assertEquals("03/01/2020", months.get(0));
//        List<IncomeByRevenueShareData> seriesData1 = incomeByRevenueShare.getSeriesData();
//        assertEquals(1, seriesData1.size());
//        assertEquals(0, bookingStatics.getPercentageOfDone());
//        List<StateStatic> seriesData2 = bookingStatics.getSeriesData();
//        assertEquals(3, seriesData2.size());
//        assertEquals(0.0f, incomeByRevenueShare.getPercentageIncrease());
//        StateStatic getResult = seriesData2.get(0);
//        assertEquals("COMPLETE", getResult.getState());
//        IncomeByTokenData getResult1 = seriesData.get(0);
//        assertEquals(0, getResult1.getToken());
//        assertEquals(0.0f, getResult1.getIncome());
//        assertEquals("03/01/2020", getResult1.getDate());
//        List<Integer> data = getResult.getData();
//        assertEquals(1, data.size());
//        assertEquals(0, data.get(0));
//        IncomeByRevenueShareData getResult2 = seriesData1.get(0);
//        assertEquals("03/01/2020", getResult2.getDate());
//        StateStatic getResult3 = seriesData2.get(1);
//        List<Integer> data1 = getResult3.getData();
//        assertEquals(data, data1);
//        assertEquals(1, data1.size());
//        assertEquals(0, data1.get(0));
//        StateStatic getResult4 = seriesData2.get(2);
//        List<Integer> data2 = getResult4.getData();
//        assertEquals(data1, data2);
//        assertEquals(1, data2.size());
//        assertEquals(0, data2.get(0));
//        assertEquals("PENDING", getResult4.getState());
//        assertEquals(0.0f, getResult2.getIncome());
//        assertEquals("CANCEL", getResult3.getState());
//        verify(accountRepository).findByRoleStringAndAccountStateString((List<String>) any(), (List<String>) any());
//        verify(serviceRepository).countByStatus((Status) any());
//        verify(bookingRepository).findByStateString((String) any());
//        verify(bookingRepository).findAll();
//        verify(bookingRepository).count();
//        verify(transactionRepository).findByTransactionTypeAndStatus((TransactionTypeEnum) any(),
//                (TransactionStatusEnum) any());
//        verify(settingRepository, atLeast(1)).findByKey((String) any());
//    }

}

