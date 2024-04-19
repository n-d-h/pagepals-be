package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.dtos.report.ListReportDto;
import com.pagepal.capstone.dtos.report.ReportCreateDto;
import com.pagepal.capstone.dtos.report.ReportQueryDto;
import com.pagepal.capstone.dtos.report.ReportReadDto;
import com.pagepal.capstone.dtos.transaction.ListTransactionDto;
import com.pagepal.capstone.entities.postgre.Customer;
import com.pagepal.capstone.entities.postgre.Report;
import com.pagepal.capstone.enums.ReportStateEnum;
import com.pagepal.capstone.enums.ReportTypeEnum;
import com.pagepal.capstone.mappers.ReportMapper;
import com.pagepal.capstone.mappers.TransactionMapper;
import com.pagepal.capstone.repositories.*;
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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final CustomerRepository customerRepository;
    private final DateUtils dateUtils;
    private final BookingRepository bookingRepository;
    private final ReaderRepository readerRepository;
    private final PostRepository postRepository;

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
            case POST -> {
                postRepository.findById(reportCreateDto.getReportedId())
                        .orElseThrow(() -> new EntityNotFoundException("Post not found"));
                reportPresent = reportRepository.findByReportedIdAndTypeAndCustomer(reportCreateDto.getReportedId(), ReportTypeEnum.POST, customer)
                        .orElse(null);
                if (reportPresent != null) throw new RuntimeException("Post has been reported");
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
}
