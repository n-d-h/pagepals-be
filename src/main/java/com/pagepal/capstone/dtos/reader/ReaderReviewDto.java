package com.pagepal.capstone.dtos.reader;

import com.pagepal.capstone.dtos.customer.CustomerDto;
import com.pagepal.capstone.dtos.service.ServiceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReaderReviewDto {
    private CustomerDto customer;
    private String date;
    private ServiceDto service;
    private int rating;
    private String review;
}
