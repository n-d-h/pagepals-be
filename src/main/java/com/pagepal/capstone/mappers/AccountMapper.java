package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.account.AccountDto;
import com.pagepal.capstone.entities.postgre.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDto toDto(Account account);

    Account toEntity(AccountDto accountDto);
}
