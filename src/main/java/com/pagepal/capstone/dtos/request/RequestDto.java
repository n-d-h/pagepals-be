package com.pagepal.capstone.dtos.request;

import com.pagepal.capstone.dtos.interview.InterviewDto;
import com.pagepal.capstone.dtos.reader.ReaderDto;
import com.pagepal.capstone.enums.RequestStateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private UUID id;
    private String description;
    private RequestStateEnum state;
    private String createdAt;
    private String updatedAt;
    private UUID staffId;
    private String staffName;
    private String rejectReason;
    private List<AnswerDto> answers;
    private List<InterviewDto> interviews;
    private ReaderDto reader;
    private List<RequestDto> lastRequests;
}
