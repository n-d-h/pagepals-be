package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.analytic.*;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.enums.TransactionStatusEnum;
import com.pagepal.capstone.enums.TransactionTypeEnum;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.AnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticServiceImpl implements AnalyticService {

    private final AccountRepository accountRepository;
    private final ServiceRepository serviceRepository;
    private final BookingRepository bookingRepository;

    private final String ROLE_READER = "READER";
    private final String ROLE_CUSTOMER = "CUSTOMER";
    private final String STATE_ACTIVE = "ACTIVE";
    private final String STATE_READER_ACTIVE = "READER_ACTIVE";
    private final String BOOKING_STATUS_COMPLETED = "COMPLETE";
    private final String BOOKING_STATUS_CANCELLED = "CANCEL";
    private final String BOOKING_STATUS_PENDING = "PENDING";
    private final String FORMAT_DATE = "MM/dd/yyyy";
    private final TransactionRepository transactionRepository;
    private final SettingRepository settingRepository;

    @Secured("ADMIN")
    @Override
    public AnalyticAdmin getAnalyticAdmin() {
//        AnalyticAdmin analyticAdmin = new AnalyticAdmin();
//        List<Account> accounts = accountRepository
//                .findByRoleStringAndAccountStateString(
//                        Arrays.asList(ROLE_READER, ROLE_CUSTOMER),
//                        Arrays.asList(STATE_ACTIVE, STATE_READER_ACTIVE)
//                );
//        long totalCustomer = accounts.stream()
//                .filter(account -> account.getRole().getName().equals(ROLE_CUSTOMER)).count();
//        long totalReader = accounts.stream()
//                .filter(account -> account.getRole().getName().equals(ROLE_READER)).count();
//
//        long totalService = serviceRepository.countByStatus(Status.ACTIVE);
//        long totalBooking = bookingRepository.count();
//
//        analyticAdmin.setTotalCustomers(totalCustomer);
//        analyticAdmin.setTotalReaders(totalReader);
//        analyticAdmin.setTotalService(totalService);
//        analyticAdmin.setTotalBookings(totalBooking);
//        analyticAdmin.setBookingStatics(getBookingStatics());
//        analyticAdmin.setIncomeByToken(getIncomeByToken());
//        analyticAdmin.setIncomeByRevenueShare(getIncomeByRevenueShare());
        return null;
    }

    @Secured("ADMIN")
    @Override
    public AnalyticAdmin getAnalyticAdminByDate(String startDate, String endDate) {

        // Parse the start and end dates
        LocalDate parsedStartDate = LocalDate.parse(startDate);
        LocalDate parsedEndDate = LocalDate.parse(endDate);

        AnalyticAdmin analyticAdmin = new AnalyticAdmin();
        List<Account> accounts = accountRepository
                .findByRoleStringAndAccountStateString(
                        Arrays.asList(ROLE_READER, ROLE_CUSTOMER),
                        Arrays.asList(STATE_ACTIVE, STATE_READER_ACTIVE)
                );
        long totalCustomer = accounts.stream()
                .filter(account -> account.getRole().getName().equals(ROLE_CUSTOMER)).count();
        long totalReader = accounts.stream()
                .filter(account -> account.getRole().getName().equals(ROLE_READER)).count();

        long totalService = serviceRepository.countByStatus(Status.ACTIVE);
        long totalBooking = bookingRepository.count();

        analyticAdmin.setTotalCustomers(totalCustomer);
        analyticAdmin.setTotalReaders(totalReader);
        analyticAdmin.setTotalService(totalService);
        analyticAdmin.setTotalBookings(totalBooking);
        analyticAdmin.setBookingStatics(getBookingStatics(parsedStartDate, parsedEndDate));
        analyticAdmin.setIncomeByToken(getIncomeByToken(parsedStartDate, parsedEndDate));
        analyticAdmin.setIncomeByRevenueShare(getIncomeByRevenueShare(parsedStartDate, parsedEndDate));
        return analyticAdmin;
    }

    private BookingStatics getBookingStatics(LocalDate startDate, LocalDate endDate) {
        List<String> days = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
        List<LocalDate> dateRange = startDate.datesUntil(endDate.plusDays(1), Period.ofMonths(1)).toList();

        for (var date : dateRange) {
            days.add(date.format(formatter));
        }
        //Get all bookings
        List<Booking> bookings = bookingRepository.findAll();
        List<Booking> completedBookings = new ArrayList<>();
        List<Booking> cancelledBookings = new ArrayList<>();
        List<Booking> pendingBookings = new ArrayList<>();
        bookings.forEach(booking -> {
            if (booking.getState().getName().equals(BOOKING_STATUS_COMPLETED)) {
                completedBookings.add(booking);
            } else if (booking.getState().getName().equals(BOOKING_STATUS_CANCELLED)) {
                cancelledBookings.add(booking);
            } else if (booking.getState().getName().equals(BOOKING_STATUS_PENDING)) {
                pendingBookings.add(booking);
            }
        });

        double percentage = (double) completedBookings.size() / bookings.size() * 100;
        int roundedPercentage = (int) Math.round(percentage);

        List<StateStatic> stateStatics = new ArrayList<>();

        //Divide bookings by month for each state
        stateStatics.add(getStateStatic(BOOKING_STATUS_COMPLETED, days, completedBookings));
        stateStatics.add(getStateStatic(BOOKING_STATUS_CANCELLED, days, cancelledBookings));
        stateStatics.add(getStateStatic(BOOKING_STATUS_PENDING, days, pendingBookings));

        BookingStatics bookingStatics = new BookingStatics();
        bookingStatics.setSeriesData(stateStatics);
        bookingStatics.setMonths(days);
        bookingStatics.setPercentageOfDone(roundedPercentage);

        return bookingStatics;
    }

    private StateStatic getStateStatic(String stateName, List<String> months, List<Booking> bookings) {
        List<Integer> data = new ArrayList<>();
        for (String month : months) {
            int count = 0;
            for (Booking booking : bookings) {
                LocalDate bookingDate = booking.getCreateAt().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate monthDate = LocalDate.parse(month, DateTimeFormatter.ofPattern(FORMAT_DATE));
                if (bookingDate.isBefore(monthDate.plusMonths(1))
                        && bookingDate.isAfter(monthDate.minusDays(1))) {
                    count++;
                }
            }
            data.add(count);
        }
        return new StateStatic(stateName, data);
    }

    private IncomeByToken getIncomeByToken(LocalDate startDate, LocalDate endDate) {
        List<String> days = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
        List<LocalDate> dateRange = startDate.datesUntil(endDate.plusDays(1), Period.ofMonths(1)).toList();

        for (var date : dateRange) {
            days.add(date.format(formatter));
        }

        List<Transaction> transactions = transactionRepository.findByTransactionTypeAndStatus(
                TransactionTypeEnum.DEPOSIT_TOKEN,
                TransactionStatusEnum.SUCCESS
        );

        int totalTokenSale = (int) Math.round(transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum());

        Setting setting = settingRepository.findByKey("TOKEN_PRICE").orElse(null);
        float tokenPrice = setting != null ? Float.parseFloat(setting.getValue()) : 0;
        float totalIncome = totalTokenSale * tokenPrice;

        List<IncomeByTokenData> data = getIncomeByTokenData(days, transactions, tokenPrice);

        int lastIndex = data.size() - 1;
        float percentage = 0;
        if (lastIndex >= 1 && data.get(lastIndex - 1).getToken() != 0) {
            percentage = ((float) data.get(lastIndex).getToken() / (float) data.get(lastIndex - 1).getToken()) * 100;
        }
        return new IncomeByToken(totalTokenSale, totalIncome, percentage, data);
    }

    private List<IncomeByTokenData> getIncomeByTokenData(List<String> months, List<Transaction> transactions, float tokenPrice) {
        List<IncomeByTokenData> data = new ArrayList<>();
        for (String month : months) {
            double totalTokenSale = 0;
            float totalIncome = 0;
            for (Transaction transaction : transactions) {
                LocalDate transactionDate = transaction.getCreateAt().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate monthDate = LocalDate.parse(month, DateTimeFormatter.ofPattern(FORMAT_DATE));
                if (transactionDate.isBefore(monthDate.plusMonths(1))
                        && transactionDate.isAfter(monthDate.minusDays(1))) {
                    totalTokenSale += transaction.getAmount();
                    totalIncome = (float) (totalTokenSale * tokenPrice);
                }
            }
            data.add(new IncomeByTokenData((int) totalTokenSale, totalIncome, month));
        }
        return data;
    }

    private IncomeByRevenueShare getIncomeByRevenueShare(LocalDate startDate, LocalDate endDate) {
        List<String> days = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
        List<LocalDate> dateRange = startDate.datesUntil(endDate.plusDays(1), Period.ofMonths(1)).toList();

        for (var date : dateRange) {
            days.add(date.format(formatter));
        }

        List<Booking> completedBookings = bookingRepository.findByStateString(BOOKING_STATUS_COMPLETED);

        float totalIncome = 0;

        Setting tokenSetting = settingRepository.findByKey("TOKEN_PRICE").orElse(null);
        Setting revenueShareSetting = settingRepository.findByKey("REVENUE_SHARE").orElse(null);
        float tokenPrice = tokenSetting != null ? Float.parseFloat(tokenSetting.getValue()) : 0;
        float revenueShare = revenueShareSetting != null ? Float.parseFloat(revenueShareSetting.getValue()) : 0;

        List<IncomeByRevenueShareData> data = new ArrayList<>();
        for (String month : days) {
            int totalToken = 0;
            for (Booking booking : completedBookings) {
                LocalDate bookingDate = booking.getCreateAt().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate monthDate = LocalDate.parse(month, DateTimeFormatter.ofPattern(FORMAT_DATE));
                if (bookingDate.isBefore(monthDate.plusMonths(1))
                        && bookingDate.isAfter(monthDate.minusDays(1))) {
                    totalToken += booking.getTotalPrice() != null ? booking.getTotalPrice() : 0;
                }
            }

            float income = (totalToken * tokenPrice * revenueShare / 100);
            totalIncome += income;
            data.add(new IncomeByRevenueShareData(income, month));
        }
        int lastIndex = data.size() - 1;
        float percentage = 0;
        if (lastIndex >= 1 && data.get(lastIndex - 1).getIncome() != 0) {
            percentage = (data.get(data.size() - 1).getIncome() / data.get(data.size() - 2).getIncome()) * 100;
        }

        return new IncomeByRevenueShare(totalIncome, percentage, data);
    }
}
