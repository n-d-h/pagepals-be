package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.reader.ReaderDto;
import com.pagepal.capstone.entities.postgre.Reader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper
public interface ReaderMapper {

    ReaderMapper INSTANCE = Mappers.getMapper(ReaderMapper.class);
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "toDateFormat")
    @Mapping(target = "deletedAt", source = "deletedAt", qualifiedByName = "toDateFormat")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toDateFormat")
    ReaderDto toDto(Reader reader);

    @Named("toDateFormat")
    default String toDateFormat(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return outputFormat.format(date);
    }
}
