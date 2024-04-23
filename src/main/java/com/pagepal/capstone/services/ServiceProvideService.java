package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.service.ListService;
import com.pagepal.capstone.dtos.service.QueryDto;

import java.util.UUID;

public interface ServiceProvideService {
    ListService getAllServicesByReaderId(UUID readerId, QueryDto queryDto);
    ListService getAllServicesByBookId(UUID bookId, QueryDto queryDto);
}
