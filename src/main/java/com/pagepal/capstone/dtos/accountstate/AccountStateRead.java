package com.pagepal.capstone.dtos.accountstate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountStateRead {

    private UUID id;

    private String name;

}
