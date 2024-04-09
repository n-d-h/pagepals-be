package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.request.RequestDto;
import com.pagepal.capstone.entities.postgre.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    RequestDto toDto(Request request);

}
