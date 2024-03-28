package com.pagepal.capstone.dtos.reader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReaderRequestInputDto {
    private String nickname;
    private List<String> genres;
    private List<String> languages;
    private String countryAscent;
    private String audioDescriptionUrl;
    private String introductionVideoUrl;
    private String description;
    private String avatarUrl;
}
