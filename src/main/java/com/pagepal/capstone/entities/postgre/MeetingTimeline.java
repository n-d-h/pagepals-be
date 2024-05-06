package com.pagepal.capstone.entities.postgre;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "MEETING_TIMELINE")
public class MeetingTimeline {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "userName", columnDefinition = "text")
    private String userName;

    @Column(name = "action", columnDefinition = "text")
    private String action;

    @Column(name = "time")
    private Date time;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    @JsonManagedReference
    private Meeting meeting;
}
