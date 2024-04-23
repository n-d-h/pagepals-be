package com.pagepal.capstone.dtos.account;

import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.LoginTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountReadDto {

    private UUID id;

    private String username;

    private String email;

    private String fullName;

    private String phoneNumber;

    private LoginTypeEnum loginType;

    private String createdAt ;

    private String updatedAt ;

    private String deletedAt;

    private AccountState accountState;

    private Customer customer;

    private Reader reader;

    private Role role;

    private Wallet wallet;
}
