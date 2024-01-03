package com.pagepal.capstone.entities.postgre;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pagepal.capstone.enums.CampaignEnum;
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
@Table(name = "CAMPAIGN")
@Where(clause = "status = 'ACTIVE'")
public class Campaign {
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

    @Column(name = "description")
    private String description;

    @Column(name = "start_at")
    private Date startAt;

    @Column(name = "end_at")
    private Date endAt;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "sale_limit")
    private Integer saleLimit;

    @Column(name = "exp_of_promotion")
    private Date expOfPromotion;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private CampaignEnum state;

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Promotion> promotions;
}
