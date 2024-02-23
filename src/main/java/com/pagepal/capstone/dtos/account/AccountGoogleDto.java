package com.pagepal.capstone.dtos.account;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountGoogleDto {
    private String name;
    private String email;
}
