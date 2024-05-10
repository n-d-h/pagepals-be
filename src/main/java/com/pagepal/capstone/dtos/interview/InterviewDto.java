package com.pagepal.capstone.dtos.interview;

import com.pagepal.capstone.dtos.meeting.MeetingDto;
import com.pagepal.capstone.dtos.request.RequestDto;
import com.pagepal.capstone.enums.InterviewResultEnum;
import com.pagepal.capstone.enums.InterviewStateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDto {

    private UUID id;

    private String note;

    private String interviewAt;

    private InterviewStateEnum state;

    private InterviewResultEnum result;

    private MeetingDto meeting;

}
