package com.pagepal.capstone.dtos.recording;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecordingDto {
    public String uuid;
    public String id;
    public String account_id;
    public String host_id;
    public String topic;
    public int type;
    public Date start_time;
    public String timezone;
    public String host_email;
    public int duration;
    public int total_size;
    public int recording_count;
    public String share_url;
    public ArrayList<RecordFileDto> recording_files;
    public String password;
    public String recording_play_passcode;
}
