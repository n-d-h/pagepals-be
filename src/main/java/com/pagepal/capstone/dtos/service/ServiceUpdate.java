package com.pagepal.capstone.dtos.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUpdate {
    private String shortDescription;
    private String description;
    private Integer price;
    private UUID serviceTypeId;
    private String imageUrl;
}
