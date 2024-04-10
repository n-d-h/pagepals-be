package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.request.QuestionDto;
import com.pagepal.capstone.entities.postgre.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    QuestionDto toDto(Question questionDto);
}
