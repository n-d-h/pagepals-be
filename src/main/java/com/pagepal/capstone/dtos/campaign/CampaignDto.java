package com.pagepal.capstone.dtos.campaign;

import com.pagepal.capstone.dtos.promotion.PromotionDto;
import com.pagepal.capstone.enums.CampaignEnum;
import com.pagepal.capstone.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignDto {
    private UUID id;
    private String title;
    private String description;
    private Date startAt;
    private Date endAt;
    private String imageUrl;
    private Integer saleLimit;
    private Date expOfPromotion;
    private Status status;
    private CampaignEnum state;
    private List<PromotionDto> promotions;
}
