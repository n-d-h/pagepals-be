package com.pagepal.capstone.dtos.booking;

import com.pagepal.capstone.entities.postgre.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private UUID id;
    private Integer totalPrice;
    private String promotionCode;
    private String description;
    private String review;
    private Integer rating;
    private String createAt;
    private String updateAt;
    private String deleteAt;
    private String startAt;
    private Customer customer;
    private Meeting meeting;
    private BookingState state;
    private WorkingTime workingTime;
    private Service service;
    private Seminar seminar;
    private List<Transaction> transactions;
}
