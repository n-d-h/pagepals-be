package com.pagepal.capstone.dtos.request;

import com.pagepal.capstone.dtos.reader.ReaderDto;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.enums.RequestStateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
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
    private String interviewAt;
    private UUID staffId;
    private String staffName;
    private String meetingCode;
    private String meetingPassword;
    private List<AnswerDto> answers;
    private ReaderDto reader;
}
