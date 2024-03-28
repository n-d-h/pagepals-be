package com.pagepal.capstone.dtos.request;

import com.pagepal.capstone.dtos.reader.ReaderRequestInputDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestInputDto {
    private ReaderRequestInputDto information;
    private List<AnswerInputDto> answers;
}
