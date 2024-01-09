package com.pagepal.capstone.dtos.role;

import com.pagepal.capstone.enums.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoleDto {

    @NotNull
    @NotEmpty(message = "Name is required")
    private String name;

    private Status status;
}
