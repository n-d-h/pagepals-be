package com.pagepal.capstone.dtos.account;

import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.LoginTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
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

    private Date createdAt = new Date();

    private Date updatedAt = new Date();

    private Date deletedAt;

    private AccountState accountState;

    private Customer customer;

    private Reader reader;

    private Role role;

    private List<Wallet> wallets;
}
