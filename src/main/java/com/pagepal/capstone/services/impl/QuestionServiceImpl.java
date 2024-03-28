package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.request.QuestionDto;
import com.pagepal.capstone.mappers.QuestionMapper;
import com.pagepal.capstone.repositories.QuestionRepository;
import com.pagepal.capstone.services.QuestionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public List<QuestionDto> getListQuestion() {
        return questionRepository.findAll().stream().map(QuestionMapper.INSTANCE::toDto).toList();
    }
}
