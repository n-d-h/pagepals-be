package com.pagepal.capstone.dtos.role;


import com.pagepal.capstone.enums.Status;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private UUID id;
    private String name;
    private Status status;
}
