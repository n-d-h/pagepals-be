package com.pagepal.capstone.entities.postgre;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pagepal.capstone.enums.MeetingEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MEETING")
public class Meeting implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "meeting_code")
    private String meetingCode;

    @Column(name = "password")
    private String password;

    @Column(name = "start_at")
    private Date startAt;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private MeetingEnum state;

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Booking> bookings;

    @OneToOne(mappedBy = "meeting")
    @JsonBackReference
    private Interview interview;

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<MeetingTimeline> meetingTimelines;

    @OneToOne(mappedBy = "meeting")
    @JsonBackReference
    private Event event;

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Record> records;
}
