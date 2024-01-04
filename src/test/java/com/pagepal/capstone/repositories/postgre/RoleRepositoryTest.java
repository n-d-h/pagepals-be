package com.pagepal.capstone.repositories.postgre;

import com.pagepal.capstone.entities.postgre.Role;
import com.pagepal.capstone.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {RoleRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.pagepal.capstone.entities.postgre"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldReturnRoleByName() {
        String ROLE_ADMIN = "ADMIN";

        Role role = new Role(UUID.randomUUID(), ROLE_ADMIN, Status.ACTIVE, null);
        this.roleRepository.save(role);

        Optional<Role> roleOptional = this.roleRepository.findByName(ROLE_ADMIN);

        assert roleOptional.isPresent();
        assert roleOptional.get().getName().equals(ROLE_ADMIN);
    }

    @Test
    void shouldReturnNullWhenWrongName() {
        String ROLE_ADMIN = "ADMIN";
        String ROLE_READER = "READER";

        Role roleAdmin = new Role(UUID.randomUUID(), ROLE_ADMIN, Status.ACTIVE, null);
        this.roleRepository.save(roleAdmin);

        Optional<Role> roleOptional = this.roleRepository.findByName(ROLE_READER);

        assertTrue(roleOptional.isEmpty());
    }
}
