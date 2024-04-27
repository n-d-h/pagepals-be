package com.pagepal.capstone.dtos.setting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SettingDto {
    private UUID id;
    private String key;
    private String value;

}
