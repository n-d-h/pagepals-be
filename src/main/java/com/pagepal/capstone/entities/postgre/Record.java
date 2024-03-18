package com.pagepal.capstone.entities.postgre;

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
@Table(name = "RECORD")
@Where(clause = "status = 'ACTIVE'")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "record_url")
    private String recordUrl;

    @Column(name = "date")
    private Date date;

    @Column(name = "capacity")
    private Double capacity;

    @Column(name = "description")
    private String description;

    @Column(name = "duration")
    private Double duration;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    @JsonManagedReference
    private Meeting meeting;
}
