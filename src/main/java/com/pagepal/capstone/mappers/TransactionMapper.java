package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.transaction.TransactionDto;
import com.pagepal.capstone.entities.postgre.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionDto toDto(Transaction transaction);

}
