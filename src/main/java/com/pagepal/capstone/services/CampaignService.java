package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.campaign.CampaignCreateDto;
import com.pagepal.capstone.dtos.campaign.CampaignDto;
import com.pagepal.capstone.dtos.campaign.CampaignUpdateDto;
import com.pagepal.capstone.dtos.campaign.QueryCampaignDto;

import java.util.List;

public interface CampaignService {
    List<CampaignDto> getListCampaign(QueryCampaignDto queryCampaignDto);
    CampaignDto getCampaignById(String id);
    CampaignDto createCampaign(CampaignCreateDto campaignDto);
    CampaignDto updateCampaign(String id, CampaignUpdateDto campaignDto);
    CampaignDto deleteCampaign(String id);
}
