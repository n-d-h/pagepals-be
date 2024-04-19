package com.pagepal.capstone.dtos.seminar;

import com.pagepal.capstone.entities.postgre.Book;
import com.pagepal.capstone.entities.postgre.Reader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeminarDto {
    private UUID id;
    private String title;
    private Integer limitCustomer;
    private Integer activeSlot;
    private String description;
    private String imageUrl;
    private Integer duration;
    private Integer price;
    private String startTime;
    private String status;
    private String createdAt;
    private String updatedAt;
    private Reader reader;
    private Book book;
}
