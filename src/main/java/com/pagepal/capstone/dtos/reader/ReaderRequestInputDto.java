package com.pagepal.capstone.dtos.reader;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReaderRequestInputDto {
    private String nickname;
    private List<String> genres;
    private List<String> languages;
    private String countryAccent;
    private String audioDescriptionUrl;
    private String introductionVideoUrl;
    private String description;
    private String avatarUrl;
}
