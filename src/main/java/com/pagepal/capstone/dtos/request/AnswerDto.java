package com.pagepal.capstone.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    private UUID id;
    private String content;
    private QuestionDto question;
}
