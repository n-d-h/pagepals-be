package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.dtos.report.*;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.*;
import com.pagepal.capstone.mappers.BookingMapper;
import com.pagepal.capstone.mappers.ReaderMapper;
import com.pagepal.capstone.mappers.ReportMapper;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.EmailService;
import com.pagepal.capstone.services.ReportService;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final CustomerRepository customerRepository;
    private final DateUtils dateUtils;
    private final BookingRepository bookingRepository;
    private final ReaderRepository readerRepository;
    private final BookingStateRepository bookingStateRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final AccountStateRepository accountStateRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final RoleRepository roleRepository;

    @Override
    public ReportReadDto getReportById(UUID id) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Report not found"));
        return ReportMapper.INSTANCE.toDto(report);
    }

    @Override
    public ReportReadDto createReport(ReportCreateDto reportCreateDto) {
        Customer customer = customerRepository
                .findById(reportCreateDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        Report reportPresent;
        switch (reportCreateDto.getType()) {
            case BOOKING -> {
                bookingRepository.findById(reportCreateDto.getReportedId())
                        .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
                reportPresent = reportRepository.findByReportedIdAndTypeAndCustomer(reportCreateDto.getReportedId(), ReportTypeEnum.BOOKING, customer)
                        .orElse(null);
                if (reportPresent != null) throw new RuntimeException("Booking has been reported");
            }
            case READER -> {
                readerRepository.findById(reportCreateDto.getReportedId())
                        .orElseThrow(() -> new EntityNotFoundException("Reader not found"));
                reportPresent = reportRepository.findByReportedIdAndTypeAndCustomer(reportCreateDto.getReportedId(), ReportTypeEnum.READER, customer)
                        .orElse(null);
                if (reportPresent != null) throw new RuntimeException("Reader has been reported");
            }
        }


        Report report = new Report();
        report.setType(reportCreateDto.getType());
        report.setCustomer(customer);
        report.setCreatedAt(dateUtils.getCurrentVietnamDate());
        report.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        report.setReason(reportCreateDto.getReason());
        report.setReportedId(reportCreateDto.getReportedId());
        report.setState(ReportStateEnum.PENDING);
        report = reportRepository.save(report);
        if (report == null) throw new RuntimeException("Cannot create report");
        return ReportMapper.INSTANCE.toDto(report);
    }

    @Override
    public ListReportDto getAllReports(ReportQueryDto query) {
        if (query.getPage() == null || query.getPage() < 0)
            query.setPage(0);
        if (query.getPageSize() == null || query.getPageSize() < 0)
            query.setPageSize(10);

        Pageable pageable;
        pageable = PageRequest.of(query.getPage(), query.getPageSize(), Sort.by("updatedAt").descending());

        Page<Report> reports;
        if (query.getType() == null || query.getType().isEmpty()) {
            if (query.getState() == null || query.getState().isEmpty()) {
                reports = reportRepository.findAll(pageable);
            } else {
                reports = reportRepository.findByState(ReportStateEnum.valueOf(query.getState()), pageable);
            }
        } else {
            if (query.getState() == null || query.getState().isEmpty()) {
                reports = reportRepository.findByType(ReportTypeEnum.valueOf(query.getType()), pageable);
            } else {
                reports = reportRepository.findByStateAndType(ReportStateEnum.valueOf(query.getState()), ReportTypeEnum.valueOf(query.getType()), pageable);
            }
        }

        ListReportDto list = new ListReportDto();
        if (list == null) {
            list.setList(Collections.emptyList());
            list.setPaging(null);
        } else {
            PagingDto pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(reports.getTotalPages());
            pagingDto.setTotalOfElements(reports.getTotalElements());
            pagingDto.setSort(reports.getSort().toString());
            pagingDto.setCurrentPage(reports.getNumber());
            pagingDto.setPageSize(reports.getSize());

            list.setList(reports.map(ReportMapper.INSTANCE::toDto).toList());
            list.setPaging(pagingDto);
        }

        return list;
    }

    @Override
    public List<ReportBookingDto> listReportBooking() {
        List<Report> reports = reportRepository.findByTypeAndState(ReportTypeEnum.BOOKING, ReportStateEnum.PENDING);
        List<ReportBookingDto> list = new ArrayList<>();


        for (Report report : reports) {
            boolean bookFound = false;
            for (ReportBookingDto booking : list) {
                if (booking.getBooking().getId().equals(report.getReportedId())) {
                    List<ReportReadDto> listReport = new ArrayList<>(booking.getListReport());
                    listReport.add(ReportMapper.INSTANCE.toDto(report));
                    booking.setListReport(listReport);
                    bookFound = true;
                    break;
                }
            }
            if (!bookFound) {
                ReportBookingDto reportReader = new ReportBookingDto();
                Booking findBooking = bookingRepository.findById(report.getReportedId())
                        .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
                reportReader.setBooking(BookingMapper.INSTANCE.toDto(findBooking));
                reportReader.setListReport(Collections.singletonList(ReportMapper.INSTANCE.toDto(report)));
                list.add(reportReader);
            }
        }

        for (ReportBookingDto booking : list) {
            booking.getListReport().sort(Comparator.comparing(ReportReadDto::getCreatedAt).reversed());
        }
        return list;
    }

    @Override
    public List<ReportReaderDto> listReportReader() {
        List<Report> reports = reportRepository.findByTypeAndState(ReportTypeEnum.READER, ReportStateEnum.PENDING);
        List<ReportReaderDto> list = new ArrayList<>();

        for (Report report : reports) {
            boolean readerFound = false;
            for (ReportReaderDto reader : list) {
                if (reader.getReader().getId().equals(report.getReportedId())) {
                    List<ReportReadDto> listReport = new ArrayList<>(reader.getListReport());
                    listReport.add(ReportMapper.INSTANCE.toDto(report));
                    reader.setListReport(listReport);
                    readerFound = true;
                    break;
                }
            }
            if (!readerFound) {
                ReportReaderDto reportReader = new ReportReaderDto();
                Reader findReader = readerRepository.findById(report.getReportedId())
                        .orElseThrow(() -> new EntityNotFoundException("Reader not found"));
                reportReader.setReader(ReaderMapper.INSTANCE.toDto(findReader));
                reportReader.setListReport(Collections.singletonList(ReportMapper.INSTANCE.toDto(report)));
                list.add(reportReader);
            }
        }

        for (ReportReaderDto reader : list) {
            reader.getListReport().sort(Comparator.comparing(ReportReadDto::getCreatedAt).reversed());
        }
        return list;
    }

    @Override
    public ReportGenericDto getReportGenericByReportedIdAndType(UUID id, String reportType) {

        ReportGenericDto reportGenericDto = new ReportGenericDto();
        ReportTypeEnum type = ReportTypeEnum.valueOf(reportType);

        switch (type) {
            case BOOKING -> {
                Booking booking = bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Booking not found"));
                Reader reader = booking.getWorkingTime().getReader();
                reportGenericDto.setBooking(BookingMapper.INSTANCE.toDto(booking));
                List<ReportReadDto> reports = reportRepository
                        .findByTypeAndState(ReportTypeEnum.BOOKING, ReportStateEnum.PENDING)
                        .stream().map(ReportMapper.INSTANCE::toDto).toList();
                List<ReportReadDto> reportByReader = new ArrayList<>();
                for (ReportReadDto report : reports) {
                    Booking findBooking = bookingRepository.findById(report.getReportedId())
                            .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
                    if (findBooking.getWorkingTime().getReader().getId().equals(reader.getId())) {
                        reportByReader.add(report);
                    }
                }
                reportGenericDto.setListReport(reportByReader);
            }
            case READER -> {
                Reader reader = readerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reader not found"));
                reportGenericDto.setReader(ReaderMapper.INSTANCE.toDto(reader));
                List<ReportReadDto> reports = reportRepository
                        .findByReportedIdAndType(id, type)
                        .stream().map(ReportMapper.INSTANCE::toDto).toList();
                reportGenericDto.setListReport(reports);
            }
        }

        return reportGenericDto;
    }

    @Override
    public ReportReadDto refundBookingForReport(UUID id) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Report not found"));
        if (report.getState() != ReportStateEnum.PENDING) throw new RuntimeException("Report has been processed");

        Booking booking = bookingRepository
                .findById(report.getReportedId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        BookingState state = bookingStateRepository
                .findByName("CANCEL")
                .orElseThrow(() -> new EntityNotFoundException("Booking state not found"));

        if (booking.getState().getId().equals(state.getId())) throw new RuntimeException("Booking has been cancelled");

        booking.setState(state);
        booking.setUpdateAt(dateUtils.getCurrentVietnamDate());
        booking = bookingRepository.save(booking);

        if (booking != null) {
            Wallet cusWallet = booking.getCustomer().getAccount().getWallet();
            cusWallet.setTokenAmount(cusWallet.getTokenAmount() + booking.getTotalPrice());
            cusWallet.setUpdatedAt(dateUtils.getCurrentVietnamDate());
            cusWallet = walletRepository.save(cusWallet);

            if (cusWallet == null) throw new RuntimeException("Cannot refund to customer");

            Transaction transaction = new Transaction();
            transaction.setStatus(TransactionStatusEnum.SUCCESS);
            transaction.setCreateAt(dateUtils.getCurrentVietnamDate());
            transaction.setTransactionType(TransactionTypeEnum.BOOKING_REFUND);
            transaction.setCurrency(CurrencyEnum.TOKEN);
            transaction.setBooking(booking);
            transaction.setAmount(Double.valueOf(booking.getTotalPrice()));
            transaction.setWallet(cusWallet);
            transaction = transactionRepository.save(transaction);
        }

        report.setState(ReportStateEnum.PROCESSED);
        report.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        report = reportRepository.save(report);
        return ReportMapper.INSTANCE.toDto(report);
    }

    @Override
    public ReportReadDto rejectReport(UUID id) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Report not found"));
        if (report.getState() != ReportStateEnum.PENDING) throw new RuntimeException("Report has been processed");
        report.setState(ReportStateEnum.REJECTED);
        report.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        report = reportRepository.save(report);
        return ReportMapper.INSTANCE.toDto(report);
    }

    @Override
    public Boolean acceptReportReader(UUID readerId, String reason) {

        Reader reader = readerRepository.findById(readerId).orElseThrow(() -> new EntityNotFoundException("Reader not found"));

        List<Report> reportReaders = reportRepository
                .findByReportedIdAndTypeAndState(
                        readerId,
                        ReportTypeEnum.READER,
                        ReportStateEnum.PENDING
                );

        reader.setStatus(Status.INACTIVE);
        reader.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        reader = readerRepository.save(reader);

        if (reader != null) {

            Account account = reader.getAccount();
            account.setAccountState(accountStateRepository
                    .findByNameAndStatus("ACTIVE", Status.ACTIVE)
                    .orElseThrow(() -> new EntityNotFoundException("Account state not found")
                    )
            );
            account.setUpdatedAt(dateUtils.getCurrentVietnamDate());
            account.setRole(roleRepository
                    .findByName("CUSTOMER")
                    .orElseThrow(() -> new EntityNotFoundException("Role not found"))
            );
            account = accountRepository.save(account);
            if (account != null) {
                reportReaders.forEach(report -> {
                    report.setState(ReportStateEnum.PROCESSED);
                    report.setUpdatedAt(dateUtils.getCurrentVietnamDate());
                });
                reportRepository.saveAll(reportReaders);
                emailService.sendSimpleEmail(
                        reader.getAccount().getEmail(),
                        "[PagePals]: Your PagePals reader account Status",
                        """
                                Dear %s,
                                                    
                                We hope this email finds you well. We're writing to inform you that, regrettably, your PagePals reader account has been temporarily suspended. This decision was made following a review of your account activity and is in accordance with our community guidelines.
                                                    
                                Reason for Suspension:
                                %s

                                We understand that this news may be disappointing, and we want to assure you that we take such actions seriously. Our top priority is to maintain a safe and respectful environment for all our users.

                                If you believe this suspension was a mistake or would like to appeal this decision, please don't hesitate to reach out to our support team at support@pagepals.com. We're here to assist you and address any concerns you may have.

                                In the meantime, we kindly ask for your cooperation and understanding as we work to ensure the integrity of our platform. Your patience is greatly appreciated.

                                Thank you for your understanding and cooperation in this matter.

                                Sincerely,

                                The PagePals Team

                                ---

                                PagePals - Connecting Readers Everywhere
                                Visit our website: https://pagepals-fe.vercel.app
                                """.formatted(reader.getNickname(), reason)
                );


            }

            return true;
        }

        return false;
    }


}
