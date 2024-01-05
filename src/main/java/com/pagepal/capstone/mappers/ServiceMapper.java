package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.entities.postgre.Service;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    ServiceDto toDto(Service service);

    Service toEntity(ServiceDto serviceDto);
}
