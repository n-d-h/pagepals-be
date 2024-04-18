package com.pagepal.capstone.dtos.recording;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecordFileDto {
    public String id;
    public String meeting_id;
    public Date recording_start;
    public Date recording_end;
    public String file_type;
    public String file_extension;
    public int file_size;
    public String play_url;
    public String download_url;
    public String status;
    public String recording_type;
}
