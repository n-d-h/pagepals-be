package com.pagepal.capstone.dtos.campaign;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QueryCampaignDto {
    private String search;
    private String sort;
    private Integer page;
    private Integer pageSize;
}
