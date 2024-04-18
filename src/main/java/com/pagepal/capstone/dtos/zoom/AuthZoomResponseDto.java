package com.pagepal.capstone.dtos.zoom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthZoomResponseDto {
    private String access_token;
    private String token_type;
    private Integer expires_in;
    private String scope;
}
