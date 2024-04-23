package com.pagepal.capstone.dtos.reader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReaderUpdateDto {

    private String nickname;
    private String genre;
    private String language;
    private String countryAccent;
    private String audioDescriptionUrl;
    private String description;
    private String introductionVideoUrl;
    private String avatarUrl;

}
