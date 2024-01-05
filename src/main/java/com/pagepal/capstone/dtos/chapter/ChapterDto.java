package com.pagepal.capstone.dtos.chapter;

import com.pagepal.capstone.dtos.book.BookDto;
import com.pagepal.capstone.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDto {
    private UUID id;
    private Integer chapterNumber;
    private Long pages;
    private Status status;
    private BookDto book;
}
