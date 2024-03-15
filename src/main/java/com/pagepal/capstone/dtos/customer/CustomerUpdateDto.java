package com.pagepal.capstone.dtos.customer;

import com.pagepal.capstone.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateDto {
    private String fullName;
    private GenderEnum gender;
    private String dob;
    private String imageUrl;
}
