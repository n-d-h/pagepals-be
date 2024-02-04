package com.pagepal.capstone.dtos.account;

import com.pagepal.capstone.entities.postgre.AccountState;
import com.pagepal.capstone.entities.postgre.Customer;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.enums.LoginTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
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
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Customer customer;
    private Reader reader;
    private AccountState accountState;
}
