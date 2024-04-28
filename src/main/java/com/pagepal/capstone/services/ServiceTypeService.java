package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.servicetype.ServiceTypeDto;

import java.util.List;
import java.util.UUID;

public interface ServiceTypeService {
    List<ServiceTypeDto> getListServiceTypesByService(UUID readerId, UUID bookId);
}
