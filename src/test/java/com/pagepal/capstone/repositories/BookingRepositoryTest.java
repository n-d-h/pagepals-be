//package com.pagepal.capstone.repositories;
//
//import com.pagepal.capstone.entities.postgre.*;
//import com.pagepal.capstone.enums.LoginTypeEnum;
//import com.pagepal.capstone.enums.MeetingEnum;
//import com.pagepal.capstone.enums.Status;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ContextConfiguration;
//
//import java.time.LocalDate;
//import java.util.Date;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@ContextConfiguration(classes = {
//        BookingRepository.class,
//        ReaderRepository.class,
//        AccountRepository.class,
//        AccountStateRepository.class,
//        RoleRepository.class,
//        MeetingRepository.class,
//        BookingStateRepository.class,
//})
//@EnableAutoConfiguration
//@EntityScan(basePackages = {"com.pagepal.capstone.entities.postgre"})
//@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
//public class BookingRepositoryTest {
//
//    @Autowired
//    private BookingRepository bookingRepository;
//    @Autowired
//    private ReaderRepository readerRepository;
//    @Autowired
//    private AccountRepository accountRepository;
//    @Autowired
//    private AccountStateRepository accountStateRepository;
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private MeetingRepository meetingRepository;
//    @Autowired
//    private BookingStateRepository bookingStateRepository;
//
//    AccountState accountState1 = new AccountState(
//            UUID.randomUUID(),
//            "ACTIVE",
//            Status.ACTIVE,
//            null
//    );
//    Role role1 = new Role(
//            UUID.randomUUID(),
//            "READER",
//            Status.ACTIVE,
//            null
//    );
//    Account account1 = new Account(
//            UUID.randomUUID(),
//            "username1",
//            "password1",
//            "email1",
//            "fullName1",
//            "0123456789",
//            LoginTypeEnum.NORMAL,
//            new Date(),
//            new Date(),
//            new Date(),
//            accountState1,
//            null,
//            null,
//            null,
//            null,
//            role1,
//            null,
//            null
//    );
//
//    LocalDate localDate1 = LocalDate.of(2023, 12, 21);
//    Date date1 = java.sql.Date.valueOf(localDate1);
//
//    Reader reader1 = new Reader(
//            UUID.randomUUID(),
//            "name1",
//            5,
//            "genre1",
//            "Vietnamese",
//            "accent1",
//            "link1",
//            "des1",
//            null,
//            null,
//            "vid1",
//            "avt1",
//            date1,
//            null, null, Status.ACTIVE,
//            null, true,
//            account1, null,
//            null, null, null,
//            null,
//            null
//    );
//
//    BookingState bookingState1 = new BookingState(
//            UUID.randomUUID(),
//            "ACTIVE",
//            Status.ACTIVE,
//            null
//    );
//
//    Meeting meeting = new Meeting(
//            UUID.randomUUID(),
//            "ABCD",
//            "123",
//            new Date(),
//            new Date(),
//            3,
//            MeetingEnum.AVAILABLE,
//            reader1,
//            null,
//            null,
//            null
//    );
//
//    Booking booking1 = new Booking(
//            UUID.randomUUID(),
//            100,
//            "ABCD",
//            "description",
//            "",
//            "",
//            4,
//            new Date(),
//            new Date(),
//            new Date(),
//            null,
//            new Customer(),
//            meeting,
//            bookingState1,
//            null,
//            null,
//            null,
//            null
//    );
//
//    @Test
//    void testGetListBookingByReader() {
//        roleRepository.save(role1);
//        accountStateRepository.save(accountState1);
//        accountRepository.save(account1);
//        readerRepository.save(reader1);
//        bookingStateRepository.save(bookingState1);
//        meetingRepository.save(meeting);
//        bookingRepository.save(booking1);
//
//        var result = bookingRepository.findById(booking1.getId());
//
//        assertNotNull(result);
//    }
//
//
//}
