package com.pagepal.capstone.dtos.reader;

import com.pagepal.capstone.dtos.book.BookDto;
import com.pagepal.capstone.dtos.chapter.ChapterDto;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReaderBookDto {
    private BookDto book;
    private List<ChapterDto> chapters = new ArrayList<>();
}
