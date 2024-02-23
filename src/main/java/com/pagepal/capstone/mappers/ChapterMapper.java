package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.chapter.ChapterDto;
import com.pagepal.capstone.entities.postgre.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChapterMapper {
    ChapterMapper INSTANCE = Mappers.getMapper(ChapterMapper.class);

    @Mapping(target = "services", ignore = true)
    Chapter toEntity(ChapterDto chapterDto);

    ChapterDto toDto(Chapter chapter);
}
