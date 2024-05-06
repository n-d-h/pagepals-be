package com.pagepal.capstone.entities.postgre;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pagepal.capstone.enums.InterviewResultEnum;
import com.pagepal.capstone.enums.InterviewStateEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "INTERVIEW")
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "note", columnDefinition = "text")
    private String note;

    @Column(name = "interview_at")
    private Date interviewAt;

    @Enumerated(EnumType.STRING)
    private InterviewStateEnum state;

    @Enumerated(EnumType.STRING)
    private InterviewResultEnum result;

    @OneToOne
    @JoinColumn(name = "meeting_id")
    @JsonManagedReference
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "request_id")
    @JsonManagedReference
    private Request request;


}
