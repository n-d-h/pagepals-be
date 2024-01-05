package com.pagepal.capstone.dtos.service;

import com.pagepal.capstone.dtos.chapter.ChapterDto;
import com.pagepal.capstone.dtos.reader.ReaderDto;
import com.pagepal.capstone.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCustomerDto {
    private UUID id;
    private Double price;
    private Date createdAt;
    private String description;
    private Double duration;
    private Integer totalOfReview;
    private Integer totalOfBooking;
    private Integer rating;
    private Status status;
    private ReaderDto reader;
    private ChapterDto chapter;
}
