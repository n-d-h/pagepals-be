package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.entities.postgre.Campaign;
import com.pagepal.capstone.enums.CampaignEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.postgre.CampaignRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CampaignServiceImpl.class})
@ExtendWith(SpringExtension.class)
public class CampaignServiceTest {

    @MockBean
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignServiceImpl campaignServiceImpl;

    Campaign campaign = new Campaign(
            UUID.randomUUID(),
            "campaign1",
            "campaign1mock",
            new Date(),
            new Date(),
            "",
            123,
            new Date(),
            Status.ACTIVE,
            CampaignEnum.AVAILABLE,
            null
    );

    @Test
    void testGetCampaign() {
        when(this.campaignRepository.findById(any())).thenReturn(java.util.Optional.of(campaign));

        var res = campaignServiceImpl.getCampaignById(campaign.getId().toString());

        Assertions.assertEquals(campaign.getId(), res.getId());
    }
}
