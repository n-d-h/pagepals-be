package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.request.QuestionDto;
import com.pagepal.capstone.services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @QueryMapping
    public List<QuestionDto> getListQuestion() {
        return questionService.getListQuestion();
    }
}
