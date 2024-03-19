package com.pagepal.capstone.dtos.momo;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Response {
    private String message;
    private String status;
    private String data;
}