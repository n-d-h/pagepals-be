package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.campaign.CampaignCreateDto;
import com.pagepal.capstone.dtos.campaign.CampaignDto;
import com.pagepal.capstone.entities.postgre.Campaign;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface CampaignMapper {
    CampaignMapper INSTANCE = Mappers.getMapper(CampaignMapper.class);
    CampaignDto toDto(Campaign campaign);
    Campaign toEntity(CampaignDto campaignDto);

    @Mapping(target = "startAt", source = "startAt", qualifiedByName = "stringToDate")
    @Mapping(target = "endAt", source = "endAt", qualifiedByName = "stringToDate")
    @Mapping(target = "expOfPromotion", source = "expOfPromotion", qualifiedByName = "stringToDate")
    Campaign toEntity(CampaignCreateDto campaignCreateDto);

    @Named("stringToDate")
    default Date stringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return Date.from(localDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant());
    }
}
