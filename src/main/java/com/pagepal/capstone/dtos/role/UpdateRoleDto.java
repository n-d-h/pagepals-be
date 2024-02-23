package com.pagepal.capstone.dtos.role;

import com.pagepal.capstone.enums.Status;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleDto {
    private String name;
    private Status status;
}
