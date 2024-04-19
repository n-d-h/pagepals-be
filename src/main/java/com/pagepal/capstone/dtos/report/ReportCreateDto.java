package com.pagepal.capstone.dtos.report;

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
public class ReportCreateDto {
    private String reason;
    private UUID reportedId;
    private ReportTypeEnum type;
    private UUID customerId;
}
