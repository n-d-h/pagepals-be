package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.post.PostCreateDto;
import com.pagepal.capstone.dtos.post.PostDto;
import com.pagepal.capstone.entities.postgre.Post;
import com.pagepal.capstone.entities.postgre.PostImage;
import com.pagepal.capstone.entities.postgre.Reader;
import org.junit.Ignore;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "postImages", source = "postImages", qualifiedByName = "postImagesToList")
    PostDto toDto(Post post);
//
//    @Mapping(target = "reader", source = "readerId", qualifiedByName = "readerIdToReader")
//    Post toEntity(PostDto postDto);
//
    @Named("postImagesToList")
    default List<String> readerIdToReader(List<PostImage> list) {
        List<String> resultList = new ArrayList<>();
        for (PostImage postImage : list) {
            resultList.add(postImage.getImageUrl());
        }
        return resultList;
    }
}
