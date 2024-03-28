package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.role.CreateRoleDto;
import com.pagepal.capstone.dtos.role.RoleDto;
import com.pagepal.capstone.dtos.role.UpdateRoleDto;
import com.pagepal.capstone.entities.postgre.Role;
import com.pagepal.capstone.mappers.RoleMapper;
import com.pagepal.capstone.repositories.RoleRepository;
import com.pagepal.capstone.services.RoleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Secured({"ADMIN", "STAFF"})
    @Override
    public List<RoleDto> getRoles() {
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty())
            return null;
        return roles.stream().map(RoleMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Secured({"ADMIN", "STAFF"})
    @Override
    public RoleDto getById(String id) {
        Role role = roleRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("Role not found"));
        return RoleMapper.INSTANCE.toDto(role);
    }

    @Secured({"ADMIN", "STAFF"})
    @Override
    public RoleDto create(CreateRoleDto createRoleDto) {
        Role role = RoleMapper.INSTANCE.createToEntity(createRoleDto);
        Role createdRole = roleRepository.save(role);
        return RoleMapper.INSTANCE.toDto(createdRole);
    }

    @Secured({"ADMIN", "STAFF"})
    @Override
    public RoleDto update(String id, UpdateRoleDto updateRoleDto) {
        RoleDto roleDto = getById(id);
        if (roleDto != null) {
            Role role = RoleMapper.INSTANCE.updateToEntity(updateRoleDto);
            role.setId(UUID.fromString(id));
            Role updatedRole = roleRepository.save(role);
            return RoleMapper.INSTANCE.toDto(updatedRole);
        }
        return null;
    }

    @Secured("ADMIN")
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
