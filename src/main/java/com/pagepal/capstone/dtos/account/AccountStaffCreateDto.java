package com.pagepal.capstone.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountStaffCreateDto {
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
}
