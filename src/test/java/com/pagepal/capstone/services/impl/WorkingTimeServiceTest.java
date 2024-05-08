//package com.pagepal.capstone.services.impl;
//
//import com.pagepal.capstone.entities.postgre.*;
//import com.pagepal.capstone.enums.LoginTypeEnum;
//import com.pagepal.capstone.enums.Status;
//import com.pagepal.capstone.repositories.ReaderRepository;
//import com.pagepal.capstone.repositories.WorkingTimeRepository;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.Date;
//import java.util.UUID;
//
//@ContextConfiguration(classes = {WorkingTimeServiceImpl.class})
//@ExtendWith(SpringExtension.class)
//public class WorkingTimeServiceTest {
//    @MockBean
//    private  WorkingTimeRepository workingTimeRepository;
//    @MockBean
//    private  ReaderRepository readerRepository;
//    @Autowired
//    private WorkingTimeServiceImpl workingTimeServiceImpl;
//
//    AccountState accountState1 = new AccountState(UUID.randomUUID(), "ACTIVE", Status.ACTIVE, null);
//    Role role1 = new Role(UUID.randomUUID(), "READER", Status.ACTIVE, null);
//    Account account1 = new Account(UUID.randomUUID(), "username1", "password1", "email1","fullName1","0123456789", LoginTypeEnum.NORMAL,
//            new Date(), new Date(), new Date(), accountState1,null,null, null, null, role1, null, null);
//    Reader reader = new Reader(UUID.randomUUID(), "nickname", 3, "Fiction",
//            "US", "US", "", "description", 0, 0,
//            "","avt", new Date(), new Date(), null, Status.ACTIVE,
//            null, null, account1,
//            null, null, null, null, null,null
//    );
//
//    WorkingTime workingTime = new WorkingTime(UUID.randomUUID(), new Date(), new Date(), new Date(), reader, null);
//
//}
