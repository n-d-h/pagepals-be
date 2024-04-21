package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.withdrawRequest.WithdrawRequestReadDto;
import com.pagepal.capstone.entities.postgre.WithdrawRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WithdrawRequestMapper {

    WithdrawRequestMapper INSTANCE = Mappers.getMapper(WithdrawRequestMapper.class);

    WithdrawRequestReadDto toDto(WithdrawRequest withdrawRequest);
}
