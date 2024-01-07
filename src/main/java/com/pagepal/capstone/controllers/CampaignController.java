package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.campaign.CampaignCreateDto;
import com.pagepal.capstone.dtos.campaign.CampaignDto;
import com.pagepal.capstone.dtos.campaign.CampaignUpdateDto;
import com.pagepal.capstone.dtos.campaign.QueryCampaignDto;
import com.pagepal.capstone.services.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService campaignService;

    @QueryMapping(name = "getListCampaigns")
    public List<CampaignDto> getListCampaigns(
            @Argument(name = "query") QueryCampaignDto queryCampaignDto
    ) {
        return campaignService.getListCampaign(queryCampaignDto);
    }

    @QueryMapping(name = "getCampaign")
    public CampaignDto getCampaign(
            @Argument(name = "id") String id
    ) {
        return campaignService.getCampaignById(id);
    }

    @MutationMapping(name = "createCampaign")
    public CampaignDto createCampaign(
            @Argument(name = "campaign") CampaignCreateDto campaignDto
    ) {
        return campaignService.createCampaign(campaignDto);
    }

    @MutationMapping(name = "updateCampaign")
    public CampaignDto updateCampaign(
            @Argument(name = "id") String id,
            @Argument(name = "campaign") CampaignUpdateDto campaignDto
    ) {
        return campaignService.updateCampaign(id, campaignDto);
    }

    @MutationMapping(name = "deleteCampaign")
    public CampaignDto deleteCampaign(
            @Argument(name = "id") String id
    ) {
        return campaignService.deleteCampaign(id);
    }
}
