package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.LoginTypeEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.postgre.BookRepository;
import com.pagepal.capstone.repositories.postgre.ReaderRepository;
import com.pagepal.capstone.repositories.postgre.ServiceRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.UUID;

@ContextConfiguration(classes = {ServiceProvideServiceImpl.class})
@ExtendWith(SpringExtension.class)
public class ServiceProvideServiceTest {
    @MockBean
    private ServiceRepository serviceRepository;
    @MockBean
    private ReaderRepository readerRepository;
    @MockBean
    private BookRepository bookRepository;
    @Autowired
    private ServiceProvideServiceImpl serviceProvideServiceImpl;

    UUID id = UUID.randomUUID();

    AccountState accountState1 = new AccountState(UUID.randomUUID(), "ACTIVE", Status.ACTIVE, null);
    AccountState accountState2 = new AccountState(UUID.randomUUID(), "INACTIVE", Status.ACTIVE, null);

    //Role
    Role role1 = new Role(UUID.randomUUID(), "READER", Status.ACTIVE, null);
    Role role2 = new Role(UUID.randomUUID(), "CUSTOMER", Status.ACTIVE, null);

    //Account
    Account account1 = new Account(UUID.randomUUID(), "username1", "password1", "email1","fullName1","0123456789", LoginTypeEnum.NORMAL,
            new Date(), new Date(), new Date(), accountState1, null, null, role1, null);
    Account account2 = new Account(UUID.randomUUID(), "username2", "password2", "email2","fullName1","0123456789", LoginTypeEnum.NORMAL,
            new Date(), new Date(), new Date(), accountState2, null, null, role2, null);

    Level level = new Level(UUID.randomUUID(), "name", 259.0,
            "description", Status.ACTIVE, null
    );

    Reader reader = new Reader(id, "nickname", 3, "Fiction",
            "US", "US", "", "description", "", "",
            "", 2.5, "", new Date(), new Date(), null, Status.ACTIVE, account1, level,
            null,null, null, null,
            null, null
    );

    Category category = new Category(UUID.randomUUID(), "name", "description",
            Status.ACTIVE, null
    );

    Book book = new Book(UUID.randomUUID(), "name", "long title", "author", "publisher",
            20L, "US", "overview", "imageUrl", "edition",
            new Date(), Status.ACTIVE, category, null
    );

    Chapter chapter = new Chapter(UUID.randomUUID(), 10, 20L,
            Status.ACTIVE, book,null
    );

    Service service = new Service(UUID.randomUUID(), 255.0,
            new Date(), "description", 10.0,
            1, 1, 1,
            Status.ACTIVE, null,
            reader, chapter
    );

//    @Test
//    void testGetAllServicesByReaderId() {
//        reader.setServices(List.of(service));
//
//        var queryDto = new QueryDto();
//        queryDto.setPage(0);
//        queryDto.setPageSize(10);
//        queryDto.setSearch("name");
//        queryDto.setSort("asc");
//
//        Pageable pageable = PageRequest.of(queryDto.getPage(), queryDto.getPageSize(), Sort.by("createdAt").ascending());
//
//        when(serviceRepository.findAllByReaderAndBookTitleContainsIgnoreCase(reader, queryDto.getSearch(), pageable))
//                .thenReturn(new PageImpl<>(List.of(service)));
//
//        var actual = serviceProvideServiceImpl.getAllServicesByReaderId(id, queryDto);
//
//        assert actual.size() > 0;
//    }
}
