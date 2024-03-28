package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.campaign.CampaignCreateDto;
import com.pagepal.capstone.dtos.campaign.CampaignDto;
import com.pagepal.capstone.dtos.campaign.CampaignUpdateDto;
import com.pagepal.capstone.dtos.campaign.QueryCampaignDto;
import com.pagepal.capstone.entities.postgre.Campaign;
import com.pagepal.capstone.enums.CampaignEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.CampaignMapper;
import com.pagepal.capstone.repositories.CampaignRepository;
import com.pagepal.capstone.services.CampaignService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;

    @Secured("STAFF")
    @Override
    public List<CampaignDto> getListCampaign(QueryCampaignDto query) {
        if (query.getPage() == null || query.getPage() < 0)
            query.setPage(0);

        if (query.getPage() > 0)
            query.setPage(query.getPage() - 1);

        if (query.getPageSize() == null || query.getPageSize() < 0)
            query.setPageSize(10);

        Pageable pageable;
        if (query.getSort() != null && query.getSort().equals("desc")) {
            pageable = PageRequest.of(query.getPage(), query.getPageSize(), Sort.by("startAt").descending());
        } else {
            pageable = PageRequest.of(query.getPage(), query.getPageSize(), Sort.by("startAt").ascending());
        }

        if (query.getSearch() == null)
            query.setSearch("");

        if (query.getSearch().equals("")) {
            var campaigns = campaignRepository.findAll(pageable);
            return campaigns.stream().map(CampaignMapper.INSTANCE::toDto).collect(Collectors.toList());
        } else {
            var res = campaignRepository.findAllCampaignWithSearch(query.getSearch(), pageable);
            return res.stream().map(CampaignMapper.INSTANCE::toDto).collect(Collectors.toList());
        }
    }

    @Secured("STAFF")
    @Override
    public CampaignDto getCampaignById(String id) {
        var campaign = campaignRepository.findById(UUID.fromString(id))
                .orElse(null);
        if (campaign == null)
            return null;
        return CampaignMapper.INSTANCE.toDto(campaign);
    }

    @Secured("STAFF")
    @Override
    public CampaignDto createCampaign(CampaignCreateDto campaignDto) {
        Campaign campaign = CampaignMapper.INSTANCE.toEntity(campaignDto);
        var res = campaignRepository.save(campaign);
        return CampaignMapper.INSTANCE.toDto(res);
    }

    @Secured("STAFF")
    @Override
    public CampaignDto updateCampaign(String id, CampaignUpdateDto campaignDto) {
        Campaign campaign = campaignRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("Campaign not found"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate startDate = LocalDate.parse(campaignDto.getStartAt(), formatter);
        LocalDate endDate = LocalDate.parse(campaignDto.getEndAt(), formatter);
        LocalDate expiredDate = LocalDate.parse(campaignDto.getExpOfPromotion(), formatter);

        campaign.setTitle(campaignDto.getTitle());
        campaign.setDescription(campaignDto.getDescription());
        campaign.setStartAt(Date.from(startDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        campaign.setEndAt(Date.from(endDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        campaign.setExpOfPromotion(Date.from(expiredDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        campaign.setSaleLimit(campaignDto.getSaleLimit());
        campaign.setStatus(Status.ACTIVE);
        campaign.setState(CampaignEnum.AVAILABLE);

        var res = campaignRepository.save(campaign);
        return CampaignMapper.INSTANCE.toDto(res);
    }

    @Secured("STAFF")
    @Override
    public CampaignDto deleteCampaign(String id) {
        Campaign campaign = campaignRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("Campaign not found"));
        campaign.setStatus(Status.INACTIVE);
        var res = campaignRepository.save(campaign);
        return CampaignMapper.INSTANCE.toDto(res);
    }
}
