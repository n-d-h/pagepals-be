package com.pagepal.capstone.dtos.report;

import com.pagepal.capstone.entities.postgre.Customer;
import com.pagepal.capstone.enums.ReportStateEnum;
import com.pagepal.capstone.enums.ReportTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportReadDto {
    private UUID id;

    private UUID reportedId;

    private String createdAt;

    private String updatedAt;

    private String reason;

    private ReportTypeEnum type;

    private ReportStateEnum state;

    private Customer customer;
}
