package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.customer.CustomerDto;
import com.pagepal.capstone.dtos.customer.CustomerReadDto;
import com.pagepal.capstone.entities.postgre.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "dob", source = "dob", qualifiedByName = "toDateFormat")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toDateFormat")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "toDateFormat")
    @Mapping(target = "deletedAt", source = "deletedAt", qualifiedByName = "toDateFormat")
    CustomerDto toDto(Customer customer);

    CustomerReadDto toDtoRead(Customer customer);

    @Named("toDateFormat")
    default String toDateFormat(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return outputFormat.format(date);
    }
}
