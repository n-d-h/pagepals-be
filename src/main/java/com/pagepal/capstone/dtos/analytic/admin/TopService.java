package com.pagepal.capstone.dtos.analytic.admin;

import com.pagepal.capstone.dtos.service.ServiceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TopService {
    private ServiceDto service;
    private Integer totalBooking;
}
