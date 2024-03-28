package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.request.QuestionDto;

import java.util.List;

public interface QuestionService {
    List<QuestionDto> getListQuestion();
}
