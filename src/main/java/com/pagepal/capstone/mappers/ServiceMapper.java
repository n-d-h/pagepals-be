package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.service.ServiceCustomerDto;
import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.entities.postgre.Service;
import org.mapstruct.Mapper;
import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.entities.postgre.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    @Mapping(target = "bookingDetails", source = "bookingDetails")
    @Mapping(target = "reader", source = "reader")
    @Mapping(target = "chapter", source = "chapter")
    ServiceDto toDto(Service book);

    ServiceCustomerDto toCustomerDto(Service book);

    Service toEntity(ServiceDto serviceDto);
}
