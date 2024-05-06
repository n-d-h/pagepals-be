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

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "READER")
//@Where(clause = "status = 'ACTIVE'")
public class Reader implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nickname", columnDefinition = "text")
    private String nickname;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "genre", columnDefinition = "text")
    private String genre;

    @Column(name = "language", columnDefinition = "text")
    private String language;

    @Column(name = "country_accent", columnDefinition = "text")
    private String countryAccent;

    @Column(name = "audio_description_url", columnDefinition = "text")
    private String audioDescriptionUrl;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "total_of_reviews")
    private Integer totalOfReviews;

    @Column(name = "total_of_bookings")
    private Integer totalOfBookings;

    @Column(name = "introduction_video_url", columnDefinition = "text")
    private String introductionVideoUrl;

    @Column(name = "thumbnail_url", columnDefinition = "text")
    private String thumbnailUrl;

    @Column(name = "avatar_url", columnDefinition = "text")
    private String avatarUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "is_updating")
    private Boolean isUpdating;

    @ManyToOne
    @JoinColumn(name = "reader_request_reference_id")
    @JsonManagedReference
    private Reader readerRequestReference;

    @OneToOne
    @JoinColumn(name = "account_id")
    @JsonManagedReference
    private Account account;

    @OneToMany(mappedBy = "reader", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<WorkingTime> workingTimes;

    @OneToMany(mappedBy = "reader", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Service> services;

    @OneToMany(mappedBy = "reader", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Request> requests;

    @OneToMany(mappedBy = "reader", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Seminar> seminars;

    @OneToMany(mappedBy = "reader", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<WithdrawRequest> withdrawRequests;
}
