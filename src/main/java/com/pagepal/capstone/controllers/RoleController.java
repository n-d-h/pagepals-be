package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.role.CreateRoleDto;
import com.pagepal.capstone.dtos.role.RoleDto;
import com.pagepal.capstone.dtos.role.UpdateRoleDto;
import com.pagepal.capstone.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @QueryMapping
    public List<RoleDto> getRoles() {
        return roleService.getRoles();
    }

    @QueryMapping
    public RoleDto getRoleById(@Argument String id) {
        return roleService.getById(id);
    }

    @MutationMapping(name = "createRole")
    public RoleDto createRole(@Argument(name = "createRole") @Valid CreateRoleDto createRoleDto) {
        return roleService.create(createRoleDto);
    }

    @MutationMapping(name = "updateRole")
    public RoleDto updateRole(@Argument(name = "updateRole") String id, @Argument UpdateRoleDto updateRoleDto) {
        return roleService.update(id, updateRoleDto);
    }

    @MutationMapping(name = "deleteRole")
    public RoleDto deleteRole(@Argument String id) {
        return roleService.delete(id);
    }
}
