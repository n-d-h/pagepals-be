package com.pagepal.capstone.entities.postgre;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BOOKING")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "promotion_code")
    private String promotionCode;

    @Column(name = "description")
    private String description;

    @Column(name = "review")
    private String review;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    @Column(name = "delete_at")
    private Date deleteAt;

    @Column(name = "start_at")
    private Date startAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonManagedReference
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    @JsonManagedReference
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "booking_state_id")
    @JsonManagedReference
    private BookingState state;

    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<BookingDetail> bookingDetails;

    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Transaction> transactions;
}
