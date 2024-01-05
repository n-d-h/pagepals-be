package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.service.QueryDto;
import com.pagepal.capstone.dtos.service.ServiceCustomerDto;
import com.pagepal.capstone.dtos.service.ServiceDto;

import java.util.List;
import java.util.UUID;

public interface ServiceProvideService {
    List<ServiceCustomerDto> getAllServicesByReaderId(UUID readerId, QueryDto queryDto);
    List<ServiceCustomerDto> getAllServicesByBookId(UUID bookId, QueryDto queryDto);
}
