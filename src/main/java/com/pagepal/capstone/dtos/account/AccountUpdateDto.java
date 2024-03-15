package com.pagepal.capstone.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateDto {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phoneNumber;

    private String accountState;
}
