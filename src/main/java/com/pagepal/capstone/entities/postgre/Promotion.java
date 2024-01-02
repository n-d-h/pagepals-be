package com.pagepal.capstone.entities.postgre;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pagepal.capstone.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PROMOTION")
@Where(clause = "status = 'ACTIVE'")
public class Promotion {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "begin_usage_date")
    private Date beginUsageDate;

    @Column(name = "end_usage_date")
    private Date endUsageDate;

    @Column(name = "number_of_remain")
    private Integer numberOfRemain;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "promotion_type_id")
    @JsonManagedReference
    private PromotionType promotionType;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    @JsonManagedReference
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    @JsonManagedReference
    private Reader reader;
}
