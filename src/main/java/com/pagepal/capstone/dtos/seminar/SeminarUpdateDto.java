package com.pagepal.capstone.dtos.seminar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeminarUpdateDto {
    private String title;
    private Integer limitCustomer;
    private Integer activeSlot;
    private String description;
    private String imageUrl;
    private Integer duration;
    private Integer price;
    private String startTime;
}
