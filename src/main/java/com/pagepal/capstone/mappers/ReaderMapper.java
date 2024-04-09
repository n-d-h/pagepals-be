package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.reader.ReaderDto;
import com.pagepal.capstone.entities.postgre.Reader;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReaderMapper {

    ReaderMapper INSTANCE = Mappers.getMapper(ReaderMapper.class);

    ReaderDto toDto(Reader reader);

}
