package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.post.PostCreateDto;
import com.pagepal.capstone.dtos.post.PostDto;
import com.pagepal.capstone.entities.postgre.Post;
import com.pagepal.capstone.entities.postgre.Reader;
import org.junit.Ignore;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PostMapper {
//    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);
//
//    @Mapping(target = "readerId", source = "reader.id")
//    PostDto toDto(Post post);
//
//    @Mapping(target = "reader", source = "readerId", qualifiedByName = "readerIdToReader")
//    Post toEntity(PostDto postDto);
//
//    @Named("readerIdToReader")
//    default Reader readerIdToReader(UUID readerId) {
//        Reader reader = new Reader();
//        reader.setId(readerId);
//        return reader;
//    }
}
