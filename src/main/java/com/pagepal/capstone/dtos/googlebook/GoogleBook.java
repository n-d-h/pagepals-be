package com.pagepal.capstone.dtos.googlebook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GoogleBook {
    private String id;
    private VolumeInfo volumeInfo;
}

