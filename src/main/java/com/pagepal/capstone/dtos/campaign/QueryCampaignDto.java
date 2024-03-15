package com.pagepal.capstone.dtos.campaign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
