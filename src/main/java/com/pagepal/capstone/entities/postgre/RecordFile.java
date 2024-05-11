package com.pagepal.capstone.entities.postgre;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pagepal.capstone.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RECORD_FILE")
public class RecordFile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "download_url", columnDefinition = "text")
    private String downloadUrl;

    @Column(name = "play_url", columnDefinition = "text")
    private String playUrl;

    @Column(name = "file_extention", columnDefinition = "text")
    private String fileExtention;

    @Column(name = "file_type", columnDefinition = "text")
    private String fileType;

    @Column(name = "start_at")
    private Date startAt;

    @Column(name = "end_at")
    private Date endAt;

    @Column(name = "recording_type", columnDefinition = "text")
    private String recordingType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "record_id")
    @JsonBackReference
    private Record record;
}
