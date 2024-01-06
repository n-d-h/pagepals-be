package com.pagepal.capstone.dtos.customer;

import com.pagepal.capstone.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateDto {
    private String fullName;
    private GenderEnum gender;
    private String dob;
    private String imageUrl;
}
