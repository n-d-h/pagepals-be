package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.accountstate.AccountStateRead;
import com.pagepal.capstone.entities.postgre.AccountState;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountStateMapper {
    AccountStateMapper INSTANCE = Mappers.getMapper(AccountStateMapper.class);

    AccountStateRead toDto(AccountState accountState);
}
