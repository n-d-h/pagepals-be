package com.pagepal.capstone.repositories.postgre;

import com.pagepal.capstone.entities.postgre.Campaign;
import com.pagepal.capstone.enums.CampaignEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.CampaignRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.UUID;

@ContextConfiguration(classes = {CampaignRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.pagepal.capstone.entities.postgre"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
public class CampaignRepositoryTest {
    @Autowired
    private CampaignRepository campaignRepository;

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
        campaignRepository.save(campaign);
        var campaign = campaignRepository.findAll();
        Assertions.assertTrue(campaign.size() > 0);
    }
}
