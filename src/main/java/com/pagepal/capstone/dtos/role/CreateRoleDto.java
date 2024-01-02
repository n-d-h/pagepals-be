package com.pagepal.capstone.dtos.role;

import com.pagepal.capstone.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoleDto {
    private String name;
    private Status status;
}
