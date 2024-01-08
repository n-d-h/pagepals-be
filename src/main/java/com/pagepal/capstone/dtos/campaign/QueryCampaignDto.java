package com.pagepal.capstone.dtos.campaign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryCampaignDto {
    private String search;
    private String sort;
    private Integer page;
    private Integer pageSize;
}
