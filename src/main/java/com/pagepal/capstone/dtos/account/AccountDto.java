package com.pagepal.capstone.dtos.account;

import com.pagepal.capstone.dtos.role.RoleDto;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.LoginTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phoneNumber;
    private LoginTypeEnum loginType;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private Customer customer;
    private Reader reader;
    private RoleDto role;
    private AccountState accountState;
    private Wallet wallet;
}
