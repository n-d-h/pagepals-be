package com.pagepal.capstone.dtos.accountstate;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountStateRead {

    private UUID id;

    private String name;

}
