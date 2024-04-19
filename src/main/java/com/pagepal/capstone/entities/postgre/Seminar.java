package com.pagepal.capstone.entities.postgre;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pagepal.capstone.enums.SeminarStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Table(name = "SEMINAR")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "status = 'ACTIVE'")
public class Seminar {
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

    @Column(name = "limit_customer")
    private Integer limitCustomer;

    @Column(name = "active_slot")
    private Integer activeSlot;

    @Column(name = "description")
    private String description;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "price")
    private Integer price;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SeminarStatus status;

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

    @OneToOne(mappedBy = "seminar", fetch = FetchType.LAZY)
    @JsonBackReference
    private Meeting meeting;

    @OneToMany(mappedBy = "seminar", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Booking> bookings;
}
