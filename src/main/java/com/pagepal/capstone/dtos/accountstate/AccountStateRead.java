package com.pagepal.capstone.dtos.accountstate;

import com.pagepal.capstone.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
