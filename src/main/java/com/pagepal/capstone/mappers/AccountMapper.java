package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.account.AccountDto;
import com.pagepal.capstone.dtos.account.AccountReadDto;
import com.pagepal.capstone.entities.postgre.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "deletedAt", source = "deletedAt", qualifiedByName = "toDateFormat")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toDateFormat")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "toDateFormat")
    AccountDto toDto(Account account);

    @Mapping(target = "accountState", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "reader", ignore = true)
    @Mapping(target = "role", ignore = true)
    Account toEntity(AccountDto accountDto);

    AccountReadDto toAccountReadDto(Account account);

    @Named("toDateFormat")
    default String toDateFormat(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return outputFormat.format(date);
    }

}
