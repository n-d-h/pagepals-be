package com.pagepal.capstone.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountStaffResponse {
    private String username;
    private String password;
    private String accessToken;
    private String refreshToken;
}
