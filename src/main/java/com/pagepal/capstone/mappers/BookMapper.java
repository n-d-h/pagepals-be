package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.book.BookDto;
import com.pagepal.capstone.entities.postgre.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDto toDto(Book book);

    @Mapping(target = "services", ignore = true)
    Book toEntity(BookDto bookDto);


}
