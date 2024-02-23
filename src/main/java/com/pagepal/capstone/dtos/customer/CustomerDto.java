package com.pagepal.capstone.dtos.customer;

import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.Booking;
import com.pagepal.capstone.entities.postgre.Follow;
import com.pagepal.capstone.enums.GenderEnum;
import com.pagepal.capstone.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CustomerDto {

    private UUID id;

    private String fullName;

    private GenderEnum gender;

    private Date dob;

    private String imageUrl;

    private Date createdAt = new Date();

    private Date updatedAt = new Date();

    private Date deletedAt;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private Account account;

    private List<Follow> follows;

    private List<Booking> bookings;
}
