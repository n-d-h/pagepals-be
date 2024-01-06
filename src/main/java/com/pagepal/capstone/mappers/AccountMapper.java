package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.account.AccountDto;
import com.pagepal.capstone.dtos.account.AccountReadDto;
import com.pagepal.capstone.entities.postgre.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDto toDto(Account account);

    @Mapping(target = "accountState", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "reader", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "wallets", ignore = true)
    Account toEntity(AccountDto accountDto);

    AccountReadDto toAccountReadDto(Account account);
}
