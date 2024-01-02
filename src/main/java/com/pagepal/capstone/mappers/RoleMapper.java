package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.role.CreateRoleDto;
import com.pagepal.capstone.dtos.role.RoleDto;
import com.pagepal.capstone.dtos.role.UpdateRoleDto;
import com.pagepal.capstone.entities.postgre.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDto toDto(Role role);

    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "id", ignore = true)
    Role createToEntity(CreateRoleDto createRoleDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    Role updateToEntity(UpdateRoleDto updateRoleDto);
}
