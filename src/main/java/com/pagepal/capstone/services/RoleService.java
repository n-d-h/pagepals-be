package com.pagepal.capstone.services;


import com.pagepal.capstone.dtos.role.CreateRoleDto;
import com.pagepal.capstone.dtos.role.RoleDto;
import com.pagepal.capstone.dtos.role.UpdateRoleDto;

import java.util.List;

public interface RoleService {
    List<RoleDto> getRoles();
    RoleDto getById(String id);
    RoleDto create(CreateRoleDto createRoleDto);
    RoleDto update(String id, UpdateRoleDto updateRoleDto);
    RoleDto delete(String id);
}
