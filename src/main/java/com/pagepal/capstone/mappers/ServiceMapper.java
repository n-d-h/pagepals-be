package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.service.ServiceCustomerDto;
import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.entities.postgre.Service;
import org.mapstruct.Mapper;
import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.dtos.service.WriteServiceDto;
import com.pagepal.capstone.entities.postgre.Chapter;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface ServiceMapper {
    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    @Mapping(target = "bookingDetails", source = "bookingDetails")
    @Mapping(target = "reader", source = "reader")
    @Mapping(target = "chapter", source = "chapter")
    ServiceDto toDto(Service book);

    @Mapping(target = "bookingDetails", ignore = true)
    @Mapping(target = "reader", source = "readerId", qualifiedByName = "readerIdToReader")
    @Mapping(target = "chapter", source = "chapterId", qualifiedByName = "chapterIdToChapter")
    Service writeService(WriteServiceDto bookDto);

    @Named("readerIdToReader")
    default Reader readerIdToReader(UUID id) {
        Reader reader = new Reader();
        reader.setId(id);
        return reader;
    }

    @Named("chapterIdToChapter")
    default Chapter chapterIdToChapter(UUID id) {
        Chapter chapter = new Chapter();
        chapter.setId(id);
        return chapter;
    }
    ServiceCustomerDto toCustomerDto(Service book);

    Service toEntity(ServiceDto serviceDto);
}
