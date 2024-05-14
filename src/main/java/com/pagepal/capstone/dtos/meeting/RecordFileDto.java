package com.pagepal.capstone.dtos.meeting;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pagepal.capstone.entities.postgre.Record;
import com.pagepal.capstone.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecordFileDto {

    private UUID id;

    private String downloadUrl;

    private String playUrl;

    private String fileExtention;

    private String fileType;

    private Date startAt;

    private Date endAt;

    private String recordingType;

    private Status status;
}
