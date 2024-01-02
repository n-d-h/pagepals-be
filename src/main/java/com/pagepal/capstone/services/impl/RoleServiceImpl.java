package com.pagepal.capstone.services.impl;


import com.pagepal.capstone.dtos.role.CreateRoleDto;
import com.pagepal.capstone.dtos.role.RoleDto;
import com.pagepal.capstone.dtos.role.UpdateRoleDto;
import com.pagepal.capstone.entities.postgre.Role;
import com.pagepal.capstone.mappers.RoleMapper;
import com.pagepal.capstone.repositories.postgre.RoleRepository;
import com.pagepal.capstone.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<RoleDto> getRoles() {
        List<Role> roles = roleRepository.findAll();
        if(roles == null || roles.isEmpty())
            return null;
        return roles.stream().map(RoleMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Override
    public RoleDto getById(String id) {
        Optional<Role> role = roleRepository.findById(UUID.fromString(id));
        return role.map(RoleMapper.INSTANCE::toDto).orElse(null);
    }

    @Override
    public RoleDto create(CreateRoleDto createRoleDto) {
        UUID id = UUID.randomUUID();
        Role role = RoleMapper.INSTANCE.toEntity(createRoleDto);
        role.setId(id);
        Role createdRole = roleRepository.save(role);
        return RoleMapper.INSTANCE.toDto(createdRole);
    }

    @Override
    public RoleDto update(String id, UpdateRoleDto updateRoleDto) {
        RoleDto roleDto = getById(id);
        if (roleDto != null) {
            Role role = RoleMapper.INSTANCE.toEntity(updateRoleDto);
            role.setId(UUID.fromString(id));
            Role updatedRole = roleRepository.save(role);
            return RoleMapper.INSTANCE.toDto(updatedRole);
        }
        return null;
    }

    @Override
    public RoleDto delete(String id) {
        RoleDto roleDto = getById(id);
        if (roleDto != null) {
            roleRepository.deleteById(UUID.fromString(id));
            return roleDto;
        }
        return null;
    }
}
