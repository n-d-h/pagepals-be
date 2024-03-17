package com.pagepal.capstone.dtos.googlebook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookSearchResult {
    private int totalItems;
    private List<GoogleBook> items;
}
