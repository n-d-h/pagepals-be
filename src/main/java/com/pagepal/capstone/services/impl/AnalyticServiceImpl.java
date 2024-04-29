package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.analytic.admin.*;
import com.pagepal.capstone.dtos.analytic.reader.ReaderStatistics;
import com.pagepal.capstone.dtos.zoom.ZoomPlan;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.enums.TransactionStatusEnum;
import com.pagepal.capstone.enums.TransactionTypeEnum;
import com.pagepal.capstone.mappers.ReaderMapper;
import com.pagepal.capstone.mappers.ServiceMapper;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.AnalyticService;
import com.pagepal.capstone.services.ZoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    private final ZoomService zoomService;
    private final ReaderRepository readerRepository;

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

        ZoomPlan zoomPlan = zoomService.getZoomPlan();

        FreeStorage freeStorage = new FreeStorage();
        freeStorage.setTotalStorage(zoomPlan.getPlan_recording().getFree_storage());
        freeStorage.setUsedStorage(zoomPlan.getPlan_recording().getFree_storage_usage());

        CloudStorage cloudStorage = new CloudStorage();
        cloudStorage.setTotalStorage(zoomPlan.getPlan_recording().getPlan_storage());
        cloudStorage.setUsedStorage(zoomPlan.getPlan_recording().getPlan_storage_usage());

        analyticAdmin.setTotalCustomers(totalCustomer);
        analyticAdmin.setTotalReaders(totalReader);
        analyticAdmin.setTotalService(totalService);
        analyticAdmin.setTotalBookings(totalBooking);
        analyticAdmin.setFreeStorage(freeStorage);
        analyticAdmin.setCloudStorage(cloudStorage);
        analyticAdmin.setTopServices(getTopServices(parsedStartDate, parsedEndDate));
        analyticAdmin.setTopReaders(getTopReaders(parsedStartDate, parsedEndDate));
        analyticAdmin.setBookingStatics(getBookingStatics(parsedStartDate, parsedEndDate));
        analyticAdmin.setIncomeByToken(getIncomeByToken(parsedStartDate, parsedEndDate));
        analyticAdmin.setCirculatingToken(getCirculatingToken(parsedStartDate, parsedEndDate));
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

    private CirculatingToken getCirculatingToken(LocalDate startDate, LocalDate endDate) {
        List<String> days = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
        List<LocalDate> dateRange = startDate.datesUntil(endDate.plusDays(1), Period.ofMonths(1)).toList();

        for (var date : dateRange) {
            days.add(date.format(formatter));
        }

        List<Transaction> transactions = transactionRepository.findByTransactionTypeAndStatus(
                TransactionTypeEnum.BOOKING_PAYMENT,
                TransactionStatusEnum.SUCCESS
        );

        List<Transaction> transactionRefunds = transactionRepository.findByTransactionTypeAndStatus(
                TransactionTypeEnum.BOOKING_REFUND,
                TransactionStatusEnum.SUCCESS
        );

        int totalTokenSale = (int) Math.round(transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum());

        int totalTokenRefund = (int) Math.round(transactionRefunds.stream()
                .mapToDouble(Transaction::getAmount)
                .sum());

        int totalToken = totalTokenSale - totalTokenRefund;

        List<CirculatingTokenData> data = getCirculatingTokenData(days, transactions);

        List<CirculatingTokenData> dataRefund = getCirculatingTokenData(days, transactionRefunds);

        for (int i = 0; i < data.size(); i++) {
            data.get(i).setToken(data.get(i).getToken() - dataRefund.get(i).getToken());
        }

        int lastIndex = data.size() - 1;
        float percentage = 0;
        if (lastIndex >= 1 && data.get(lastIndex - 1).getToken() != 0) {
            percentage = ((float) data.get(lastIndex).getToken() / (float) data.get(lastIndex - 1).getToken()) * 100;
        }
        return new CirculatingToken(totalToken, percentage, data);
    }

    private List<CirculatingTokenData> getCirculatingTokenData(List<String> months, List<Transaction> transactions) {
        List<CirculatingTokenData> data = new ArrayList<>();
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
                }
            }
            data.add(new CirculatingTokenData((int) totalTokenSale, month));
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

    private List<TopReader> getTopReaders(LocalDate startDate, LocalDate endDate) {

        List<Transaction> transactions = transactionRepository.findByCreateAtBetweenAndTransactionTypeAndStatus(
                Date.valueOf(startDate),
                Date.valueOf(endDate),
                TransactionTypeEnum.BOOKING_DONE_RECEIVE,
                TransactionStatusEnum.SUCCESS
        );

        Map<Wallet, List<Transaction>> walletTransactionsMap = new HashMap<>();
        for (Transaction transaction : transactions) {
            Wallet wallet = transaction.getWallet();
            List<Transaction> walletTransactions = walletTransactionsMap.getOrDefault(wallet, new ArrayList<>());
            walletTransactions.add(transaction);
            walletTransactionsMap.put(wallet, walletTransactions);
        }

        List<TopReader> topReaders = new ArrayList<>();
        for (Map.Entry<Wallet, List<Transaction>> entry : walletTransactionsMap.entrySet()) {
            Wallet wallet = entry.getKey();
            List<Transaction> walletTransactions = entry.getValue();
            float totalIncome = (float) walletTransactions.stream()
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            Reader reader = wallet.getAccount().getReader();
            topReaders.add(new TopReader(ReaderMapper.INSTANCE.toDto(reader), totalIncome));
        }

        topReaders.sort(Comparator.comparingDouble(TopReader::getTotalIncome).reversed());

        return topReaders.stream()
                .limit(5)
                .toList();
    }

    private List<TopService> getTopServices(LocalDate startDate, LocalDate endDate) {
        List<Booking> bookings = bookingRepository
                .findByCreateAtBetweenAndStateAndServiceNotNull(
                        Date.valueOf(startDate),
                        Date.valueOf(endDate),
                        "COMPLETE"
                );

        Map<com.pagepal.capstone.entities.postgre.Service, List<Booking>> serviceBookingMap = new HashMap<>();
        for (Booking booking : bookings) {
            var service = booking.getService();
            List<Booking> serviceBookings = serviceBookingMap.getOrDefault(service, new ArrayList<>());
            serviceBookings.add(booking);
            serviceBookingMap.put(service, serviceBookings);
        }

        List<TopService> topServices = new ArrayList<>();
        for (Map.Entry<com.pagepal.capstone.entities.postgre.Service, List<Booking>> entry : serviceBookingMap.entrySet()) {
            com.pagepal.capstone.entities.postgre.Service service = entry.getKey();
            List<Booking> serviceBookings = entry.getValue();
            int totalBooking = serviceBookings.size();
            topServices.add(new TopService(ServiceMapper.INSTANCE.toDto(service), totalBooking));
        }

        topServices.sort(Comparator.comparingInt(TopService::getTotalBooking).reversed());

        return topServices.stream()
                .limit(5)
                .toList();
    }

    @Secured("READER")
    @Override
    public ReaderStatistics getReaderStatistics(UUID id, String startDate, String endDate) {
        // Parse the start and end dates
        LocalDate parsedStartDate = LocalDate.parse(startDate);
        LocalDate parsedEndDate = LocalDate.parse(endDate);

        ReaderStatistics statistics = new ReaderStatistics();
        List<LocalDate> chartDates = generateChartDates(parsedStartDate, parsedEndDate);
        var milestones = chartDates.stream().map(date -> date.format(DateTimeFormatter.ofPattern(FORMAT_DATE))).toList();

        var completedBookings = bookingRepository.findByCreateAtBetweenAndReaderIdAndState(
                Date.valueOf(parsedStartDate),
                Date.valueOf(parsedEndDate),
                id,
                BOOKING_STATUS_COMPLETED
        );
        var canceledBookings = bookingRepository.findByCreateAtBetweenAndReaderIdAndState(
                Date.valueOf(parsedStartDate),
                Date.valueOf(parsedEndDate),
                id,
                BOOKING_STATUS_CANCELLED
        );

        var completedData = getReaderStateStatic(BOOKING_STATUS_COMPLETED, chartDates, completedBookings, id);
        var canceledData = getReaderStateStatic(BOOKING_STATUS_CANCELLED, chartDates, canceledBookings, id);


        var totalBookings = bookingRepository.countByCreateAtBetweenAndReaderIdAndState(
                Date.valueOf(parsedStartDate),
                Date.valueOf(parsedEndDate),
                id
        ).intValue();

        double percentage = (double) completedBookings.size() / totalBookings * 100;
        int roundedPercentage = (int) Math.round(percentage);

        var totalAmountShare = calculateAmountShare(completedBookings.size());
        var totalIncome = completedBookings.stream()
                .mapToDouble(Booking::getTotalPrice)
                .sum();
        var totalRefund = canceledBookings.stream()
                .mapToDouble(Booking::getTotalPrice)
                .sum();


        var allTimeIncome = bookingRepository.sumPriceByReaderId(id, Date.valueOf(LocalDate.now()));
        if (allTimeIncome == null) {
            allTimeIncome = 0.00;
        }

        // Set the statistics data
        statistics.setMilestones(milestones);
        statistics.setCompletedBookingData(completedData);
        statistics.setCanceledBookingData(canceledData);
        statistics.setTotalFinishBookingInThisPeriod(totalBookings);
        statistics.setSuccessBookingRate(roundedPercentage);
        statistics.setTotalIncomeInThisPeriod(String.format("%.2f", totalIncome));
        statistics.setTotalRefundInThisPeriod(String.format("%.2f", totalRefund));
        statistics.setTotalProfitInThisPeriod(String.format("%.2f", (totalIncome - totalAmountShare)));
        statistics.setTotalAmountShareInThisPeriod(String.format("%.2f", totalAmountShare));
        statistics.setAllTimeTotalFinishBooking(bookingRepository.countByReaderIdAndState(id).intValue());
        statistics.setAllTimeIncome(String.format("%.2f", allTimeIncome));
        statistics.setTotalActiveServices(serviceRepository.countActiveServicesByReaderId(id).intValue());
        return statistics;
    }

    private List<Integer> getReaderStateStatic(String stateName, List<LocalDate> mileStone, List<Booking> bookings, UUID readerId) {
        List<Integer> data = new ArrayList<>();
        Long countFirstMileStoneBooking = bookingRepository.countByCreateAtBeforeAndReaderIdAndState(
                Date.valueOf(mileStone.get(0)),
                readerId,
                stateName
        );
        data.add(countFirstMileStoneBooking.intValue());

        // Iterate through consecutive pairs of milestones
        for (int i = 0; i < mileStone.size() - 1; i++) {
            LocalDate startDate = mileStone.get(i);
            LocalDate endDate = mileStone.get(i + 1).minusDays(1); // Exclude the next milestone date itself

            int count = 0;
            for (Booking booking : bookings) {
                LocalDate bookingDate = booking.getCreateAt().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                if (bookingDate.isBefore(endDate.plusDays(1)) // Include endDate
                        && bookingDate.isAfter(startDate.minusDays(1))) { // Include startDate
                    count++;
                }
            }
            data.add(count);
        }

        return data;
    }

    private Double calculateAmountShare(Integer length) {
        var amountShare = settingRepository
                .findByKey("REVENUE_SHARE")
                .orElseThrow(() -> new IllegalStateException("Revenue share setting not found"));
        var value = Integer.parseInt(amountShare.getValue());
        return (double) (length * value) / 100;
    }

    private static List<LocalDate> generateChartDates(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        if (daysBetween <= 10) {
            // Include all days from start to end (inclusive)
            for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
                dates.add(date);
            }
        } else {
            // Calculate interval size for 10 dates (excluding start and end)
            long interval = (daysBetween - 1) / 9;

            // Add start date
            dates.add(startDate);

            // Add evenly spaced dates between start and end
            for (int i = 1; i < 9; i++) {
                LocalDate newDate = startDate.plusDays(interval * i);
                dates.add(newDate);
            }

            // Add end date if it's not already included
            if (endDate.isAfter(dates.get(dates.size() - 1))) {
                dates.add(endDate);
            }
        }

        return dates;
    }

}
