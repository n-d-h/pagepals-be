package com.pagepal.capstone.dtos.recording;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRecordings {
    public String from;
    public String to;
    public int page_count;
    public int page_size;
    public int total_records;
    public String next_page_token;
    public ArrayList<RecordingDto> meetings;
}
