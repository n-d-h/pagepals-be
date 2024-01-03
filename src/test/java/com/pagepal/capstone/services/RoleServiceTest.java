package com.pagepal.capstone.services;

import com.pagepal.capstone.entities.postgre.Role;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.postgre.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {RoleRepository.class})
public class RoleServiceTest {
    @Autowired
    private RoleRepository roleRepository;

    private final Logger LOG = LoggerFactory.getLogger(RoleServiceTest.class);

    @Before
    public void setUp() {
        Role role1 = new Role();
        role1.setName("CUSTOMER");
        role1.setStatus(Status.ACTIVE);
        roleRepository.save(role1);

        Role role2 = new Role();
        role2.setName("STAFF");
        role2.setStatus(Status.ACTIVE);
        roleRepository.save(role2);
    }

    @Test
    public void testSaveNewRole(){
        Role role = new Role();
        role.setName("ADMIN");
        role.setStatus(Status.ACTIVE);
        roleRepository.save(role);

        LOG.info("Role: {}", role);

        var findRole = roleRepository.findByName("ADMIN").orElse(null);
        LOG.info("Find role: {}", findRole);

        assert findRole != null;
    }
}
