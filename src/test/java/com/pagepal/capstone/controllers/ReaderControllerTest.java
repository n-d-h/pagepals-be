//package com.pagepal.capstone.controllers;
//
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.pagepal.capstone.dtos.reader.ReaderDto;
//import com.pagepal.capstone.entities.postgre.Account;
//import com.pagepal.capstone.entities.postgre.AccountState;
//import com.pagepal.capstone.entities.postgre.Reader;
//import com.pagepal.capstone.entities.postgre.Role;
//import com.pagepal.capstone.enums.LoginTypeEnum;
//import com.pagepal.capstone.enums.Status;
//import com.pagepal.capstone.mappers.ReaderMapper;
//import com.pagepal.capstone.repositories.AccountRepository;
//import com.pagepal.capstone.repositories.AccountStateRepository;
//import com.pagepal.capstone.repositories.ReaderRepository;
//import com.pagepal.capstone.repositories.RoleRepository;
//import com.pagepal.capstone.services.ReaderService;
//
//import java.util.*;
//
//import com.pagepal.capstone.services.impl.ReaderServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.graphql.test.tester.GraphQlTester;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//@ContextConfiguration(classes = {AccountRepository.class, AccountStateRepository.class,
//        RoleRepository.class, ReaderService.class, ReaderServiceImpl.class, ReaderRepository.class})
//@EnableAutoConfiguration
//@GraphQlTest(ReaderController.class)
//public class ReaderControllerTest {
//
//    @Autowired
//    private GraphQlTester graphQlTester;
//
//    @MockBean
//    private AccountRepository accountRepository;
//
//    @MockBean
//    private AccountStateRepository accountStateRepository;
//
//    @MockBean
//    private ReaderRepository readerRepository;
//
//    @MockBean
//    private RoleRepository roleRepository;
//    @Autowired
//    private ReaderService readerService;
//
//    //Mock data
//    //Account State
//    AccountState accountState1 = new AccountState(UUID.randomUUID(), "ACTIVE", Status.ACTIVE, null);
//    AccountState accountState2 = new AccountState(UUID.randomUUID(), "INACTIVE", Status.ACTIVE, null);
//
//    //Role
//    Role role1 = new Role(UUID.randomUUID(), "READER", Status.ACTIVE, null);
//    Role role2 = new Role(UUID.randomUUID(), "CUSTOMER", Status.ACTIVE, null);
//
//    //Account
//    Account account1 = new Account(UUID.randomUUID(), "username1", "password1", "email1", LoginTypeEnum.NORMAL,
//            new Date(), new Date(),new Date(), accountState1, null, null, role1, null);
//    Account account2 = new Account(UUID.randomUUID(), "username2", "password2", "email2", LoginTypeEnum.NORMAL,
//            new Date(), new Date(),new Date(), accountState2, null, null, role2, null);
//    //Reader
//    Reader reader1 = new Reader(UUID.fromString("1bdc853a-97de-4efb-8a07-9df6b14511e8"), "name1", 5, "genre1", "Vietnamese", "accent1" ,
//            "url" ,"des1", "123", "123", "url", 123.2, "tag",
//            new Date(), new Date(), new Date(), null, account1, null, null, null, null,
//            null, null, null);
//    Reader reader2 = new Reader(UUID.randomUUID(), "name2", 5, "genre1", "Vietnamese", "accent1" ,
//            "url" ,"des1", "123", "123", "url", 123.2, "tag",
//            new Date(), new Date(), new Date(), null, account2, null, null, null, null,
//            null, null, null);
//
//    @Test
//    void shouldGetFirstBook() {
//        ReaderDto readerDto = ReaderMapper.INSTANCE.toDto(reader1);
//        when(this.accountStateRepository.findByNameAndStatus("ACTIVE", Status.ACTIVE)).thenReturn(Optional.of(accountState1));
//        when(this.roleRepository.findByName("READER")).thenReturn(Optional.of(role1));
//        when(this.accountRepository.findByAccountStateAndRole(accountState1, role1)).thenReturn(List.of(account1));
//        when(this.readerService.getReadersActive()).thenReturn(List.of(readerDto));
//
//        this.graphQlTester
//                .documentName("Query")
//                .execute()
//                .path("getReadersActive")
//                .matchesJson("""
//                    {
//                        "id": "1bdc853a-97de-4efb-8a07-9df6b14511e8",
//                        "nickname": "name1",
//                        "language": "Vietnamese",
//                        "account":{
//                            "username": "username1"
//                        }
//                    }
//                """);
//    }
//}
//
