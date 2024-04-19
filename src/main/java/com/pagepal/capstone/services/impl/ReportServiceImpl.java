package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.dtos.report.ListReportDto;
import com.pagepal.capstone.dtos.report.ReportCreateDto;
import com.pagepal.capstone.dtos.report.ReportQueryDto;
import com.pagepal.capstone.dtos.report.ReportReadDto;
import com.pagepal.capstone.dtos.seminar.ReportBookingDto;
import com.pagepal.capstone.dtos.seminar.ReportPostDto;
import com.pagepal.capstone.dtos.seminar.ReportReaderDto;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.ReportStateEnum;
import com.pagepal.capstone.enums.ReportTypeEnum;
import com.pagepal.capstone.mappers.*;
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

    @Override
    public List<ReportBookingDto> listReportBooking() {
        List<Report> reports = reportRepository.findByTypeAndState(ReportTypeEnum.BOOKING, ReportStateEnum.PENDING);
        List<ReportBookingDto> list = new ArrayList<>();

        for (Report report : reports) {
            ReportBookingDto reportBooking = new ReportBookingDto();
            reportBooking.setListReport(Collections.singletonList(ReportMapper.INSTANCE.toDto(report)));
            Booking booking = bookingRepository.findById(report.getReportedId()).orElseThrow(() -> new EntityNotFoundException("Booking not found"));
            reportBooking.setBooking(BookingMapper.INSTANCE.toDto(booking));
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
                    reader.getListReport().add(ReportMapper.INSTANCE.toDto(report));
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
        return list;
    }

    @Override
    public List<ReportPostDto> listReportPost() {
        List<Report> reports = reportRepository.findByTypeAndState(ReportTypeEnum.POST, ReportStateEnum.PENDING);
        List<ReportPostDto> list = new ArrayList<>();

        for (Report report : reports) {
            boolean postFound = false;
            for (ReportPostDto post : list) {
                if (post.getPost().getId().equals(report.getReportedId())) {
                    post.getListReport().add(ReportMapper.INSTANCE.toDto(report));
                    postFound = true;
                    break;
                }
            }
            if (!postFound) {
                ReportPostDto reportReader = new ReportPostDto();
                Post findPost = postRepository.findById(report.getReportedId())
                        .orElseThrow(() -> new EntityNotFoundException("Post not found"));
                reportReader.setPost(PostMapper.INSTANCE.toDto(findPost));
                reportReader.setListReport(Collections.singletonList(ReportMapper.INSTANCE.toDto(report)));
                list.add(reportReader);
            }
        }
        return list;
    }
}
