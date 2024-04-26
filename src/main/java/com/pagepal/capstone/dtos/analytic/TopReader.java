package com.pagepal.capstone.dtos.analytic;

import com.pagepal.capstone.dtos.reader.ReaderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TopReader {
    ReaderDto reader;
    Float totalIncome;
}
