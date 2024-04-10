package com.pagepal.capstone.entities.postgre;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pagepal.capstone.enums.GenderEnum;
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
@Table(name = "CUSTOMER")
@Where(clause = "status = 'ACTIVE'")
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dob")
    private Date dob;

    @Column(name = "image_url")
    private String imageUrl;

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

    @OneToOne
    @JoinColumn(name = "account_id")
    @JsonManagedReference
    private Account account;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Follow> follows;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Booking> bookings;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<PostComment> postComments;
}
