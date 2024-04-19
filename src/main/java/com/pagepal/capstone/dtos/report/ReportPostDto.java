package com.pagepal.capstone.dtos.report;

import com.pagepal.capstone.dtos.post.PostDto;
import com.pagepal.capstone.dtos.report.ReportReadDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportPostDto {
    private PostDto post;
    private List<ReportReadDto> listReport;
}
