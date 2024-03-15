package com.pagepal.capstone.dtos.chapter;

import com.pagepal.capstone.dtos.book.BookDto;
import com.pagepal.capstone.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDto {
    private UUID id;
    private Integer chapterNumber;
    private Long pages;
    private Status status;
    private BookDto book;
}
