package com.pagepal.capstone.dtos.analytic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CloudStorage {
    private String totalStorage;
    private String usedStorage;
}
