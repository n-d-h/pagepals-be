package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.workingtime.WorkingTimeCreateDto;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.LoginTypeEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.postgre.ReaderRepository;
import com.pagepal.capstone.repositories.postgre.WorkingTimeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.UUID;

@ContextConfiguration(classes = {WorkingTimeServiceImpl.class})
@ExtendWith(SpringExtension.class)
public class WorkingTimeServiceTest {
    @MockBean
    private  WorkingTimeRepository workingTimeRepository;
    @MockBean
    private  ReaderRepository readerRepository;
    @Autowired
    private WorkingTimeServiceImpl workingTimeServiceImpl;

    AccountState accountState1 = new AccountState(UUID.randomUUID(), "ACTIVE", Status.ACTIVE, null);
    Role role1 = new Role(UUID.randomUUID(), "READER", Status.ACTIVE, null);
    Account account1 = new Account(UUID.randomUUID(), "username1", "password1", "email1","fullName1","0123456789", LoginTypeEnum.NORMAL,
            new Date(), new Date(), new Date(), accountState1, null, null, role1, null);
    Level level = new Level(UUID.randomUUID(), "name", 259.0,
            "description", Status.ACTIVE, null
    );
    Reader reader = new Reader(UUID.randomUUID(), "nickname", 3, "Fiction",
            "US", "US", "", "description", "", "",
            "", 2.5, "", new Date(), new Date(), null, Status.ACTIVE, account1, level,
            null, null, null, null,
            null, null
    );

    WorkingTime workingTime = new WorkingTime(UUID.randomUUID(), new Date(), new Date(), new Date(), reader, null);

    @Test
    void testCreateWorkingTime() {
        Mockito.when(readerRepository.findById(this.reader.getId())).thenReturn(
                java.util.Optional.of(this.reader)
        );

        Mockito.when(workingTimeRepository.save(Mockito.any(WorkingTime.class))).thenReturn(
                workingTime
        );

        WorkingTimeCreateDto workingTimeCreateDto = new WorkingTimeCreateDto();
        workingTimeCreateDto.setStartTime("2021-05-05 07:00:00");
        workingTimeCreateDto.setEndTime("2021-05-05 08:00:00");
        workingTimeCreateDto.setDate("2021-05-05");
        workingTimeCreateDto.setReaderId(this.reader.getId());

        var workingTimeDto = workingTimeServiceImpl.createReaderWorkingTime(workingTimeCreateDto);

        Assertions.assertNotNull(workingTimeDto);
    }
}
