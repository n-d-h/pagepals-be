package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Customer;
import com.pagepal.capstone.entities.postgre.Report;
import com.pagepal.capstone.enums.ReportStateEnum;
import com.pagepal.capstone.enums.ReportTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    Page<Report> findByState(ReportStateEnum state, Pageable pageable);

    Page<Report> findByType(ReportTypeEnum type, Pageable pageable);


    Page<Report> findByStateAndType(ReportStateEnum state, ReportTypeEnum type, Pageable pageable);

    Optional<Report> findByReportedIdAndTypeAndCustomer(UUID reportedId, ReportTypeEnum type, Customer customer);

    List<Report> findByTypeAndState(ReportTypeEnum type, ReportStateEnum state);

    List<Report> findByReportedIdAndType(UUID reportedId, ReportTypeEnum type);

    List<Report> findByReportedIdAndTypeAndState(UUID reportedId, ReportTypeEnum type, ReportStateEnum state);


}
