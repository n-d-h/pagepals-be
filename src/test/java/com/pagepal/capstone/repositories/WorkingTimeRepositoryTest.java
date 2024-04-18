package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.LoginTypeEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.ReaderRepository;
import com.pagepal.capstone.repositories.WorkingTimeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.UUID;

@ContextConfiguration(classes = {
        WorkingTimeRepository.class,
        ReaderRepository.class,
})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.pagepal.capstone.entities.postgre"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
public class WorkingTimeRepositoryTest {
    AccountState accountState1 = new AccountState(UUID.randomUUID(), "ACTIVE", Status.ACTIVE, null);
    Role role1 = new Role(UUID.randomUUID(), "READER", Status.ACTIVE, null);
    Account account1 = new Account(UUID.randomUUID(), "username1", "password1", "email1","fullName1","0123456789", LoginTypeEnum.NORMAL,
            new Date(), new Date(), new Date(), accountState1,null,null, null, null, role1, null, null);
    Level level = new Level(UUID.randomUUID(), "name", 259.0,
            "description", Status.ACTIVE, null
    );
    Reader reader = new Reader(UUID.randomUUID(), "nickname", 3, "Fiction",
            "US", "US", "", "description", 0, 0,
            "","avt",  2.5, "", new Date(), new Date(), null, Status.ACTIVE, null, null, account1, level,
            null, null, null, null,
            null, null, null
    );

    @Autowired
    private WorkingTimeRepository workingTimeRepository;
    @Autowired
    private ReaderRepository readerRepository;

    @Test
    void testCreateWorkingTime() {
        Date currentTime = new Date();
        WorkingTime workingTime = new WorkingTime();
        workingTime.setStartTime(new Date());
        workingTime.setEndTime(new Date());
        workingTime.setDate(currentTime);
        workingTime.setReader(reader);
        workingTimeRepository.save(workingTime);

        var workingTimes = workingTimeRepository.findById(workingTime.getId());

        Assertions.assertTrue(workingTimes.isPresent());
    }
}
