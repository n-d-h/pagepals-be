package com.pagepal.capstone.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateDto {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phoneNumber;
}
