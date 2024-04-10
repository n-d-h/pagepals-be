package com.pagepal.capstone.dtos.campaign;

import com.pagepal.capstone.dtos.promotion.PromotionDto;
import com.pagepal.capstone.enums.CampaignEnum;
import com.pagepal.capstone.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CampaignDto {
    private UUID id;
    private String title;
    private String description;
    private String startAt;
    private String endAt;
    private String imageUrl;
    private Integer saleLimit;
    private String expOfPromotion;
    private Status status;
    private CampaignEnum state;
    private List<PromotionDto> promotions;
}
