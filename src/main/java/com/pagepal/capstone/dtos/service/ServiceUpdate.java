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
    private String description;
    private Double price;
    private UUID serviceTypeId;
}
