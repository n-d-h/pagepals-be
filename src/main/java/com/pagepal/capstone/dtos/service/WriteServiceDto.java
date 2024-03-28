package com.pagepal.capstone.dtos.service;

import com.pagepal.capstone.dtos.googlebook.GoogleBook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WriteServiceDto {
    private Integer price;
    private String description;
    private Double duration;
    private UUID readerId;
    private UUID serviceTypeId;
    private GoogleBook book;
}
