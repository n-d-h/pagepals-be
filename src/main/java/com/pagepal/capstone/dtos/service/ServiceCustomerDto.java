package com.pagepal.capstone.dtos.service;

import com.pagepal.capstone.dtos.reader.ReaderDto;
import com.pagepal.capstone.entities.postgre.Book;
import com.pagepal.capstone.entities.postgre.ServiceType;
import com.pagepal.capstone.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCustomerDto {
    private UUID id;
    private Integer price;
    private Date createdAt;
    private String description;
    private Double duration;
    private Integer totalOfReview;
    private Integer totalOfBooking;
    private Integer rating;
    private Status status;
    private ReaderDto reader;
    private Book book;
    private ServiceType serviceType;
}
