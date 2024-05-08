package com.pagepal.capstone.entities.postgre;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pagepal.capstone.enums.SeminarStatus;
import com.pagepal.capstone.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SEMINAR")
public class Seminar {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", columnDefinition = "text")
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "imageUrl", columnDefinition = "text")
    private String imageUrl;

    @Column(name = "reject_reason", columnDefinition = "text")
    private String rejectReason;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private SeminarStatus state;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    @JsonManagedReference
    private Reader reader;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonManagedReference
    private Book book;

    @OneToMany(mappedBy = "seminar")
    @JsonBackReference
    private List<Event> events;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
