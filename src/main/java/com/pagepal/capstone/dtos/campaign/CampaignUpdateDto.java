package com.pagepal.capstone.dtos.campaign;

import com.pagepal.capstone.enums.CampaignEnum;
import com.pagepal.capstone.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignUpdateDto {
    private String title;
    private String description;
    private String startAt;
    private String endAt;
    private String imageUrl;
    private Integer saleLimit;
    private String expOfPromotion;
    private Status status;
    private CampaignEnum state;
}
