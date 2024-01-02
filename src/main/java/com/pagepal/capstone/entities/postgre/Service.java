package com.pagepal.capstone.entities.postgre;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pagepal.capstone.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SERVICE")
@Where(clause = "status = 'ACTIVE'")
public class Service {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "price")
    private Double price;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "description")
    private String description;

    @Column(name = "duration")
    private Double duration;

    @Column(name = "total_of_review")
    private Integer totalOfReview;

    @Column(name = "total_of_booking")
    private Integer totalOfBooking;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<BookingDetail> bookingDetails;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    @JsonManagedReference
    private Reader reader;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    @JsonManagedReference
    private Chapter chapter;
}
