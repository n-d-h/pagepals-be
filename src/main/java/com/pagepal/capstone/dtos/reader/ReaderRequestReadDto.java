package com.pagepal.capstone.dtos.reader;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReaderRequestReadDto {
    private UUID id;
    private ReaderDto preference;
    private String nickname;
    private List<String> genres;
    private List<String> languages;
    private String countryAccent;
    private String audioDescriptionUrl;
    private String introductionVideoUrl;
    private String thumbnailUrl;
    private String description;
    private String avatarUrl;
}
