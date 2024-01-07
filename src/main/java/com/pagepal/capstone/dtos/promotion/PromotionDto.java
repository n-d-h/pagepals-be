package com.pagepal.capstone.dtos.promotion;

import com.pagepal.capstone.dtos.campaign.CampaignDto;
import com.pagepal.capstone.dtos.reader.ReaderDto;
import com.pagepal.capstone.entities.postgre.PromotionType;
import com.pagepal.capstone.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDto {
    private UUID id;
    private String code;
    private String description;
    private Date beginUsageDate;
    private Date endUsageDate;
    private Integer numberOfRemain;
    private Status status;
    private PromotionType promotionType;
    private CampaignDto campaign;
    private ReaderDto reader;
}
