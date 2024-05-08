package com.pagepal.capstone.entities.postgre;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pagepal.capstone.enums.RequestStateEnum;
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
@Table(name = "REQUEST")
public class Request implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "reject_reason", columnDefinition = "text")
    private String rejectReason;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "staff_id")
    private UUID staffId;

    @Column(name = "staff_name")
    private String staffName;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private RequestStateEnum state;

    @OneToMany(mappedBy = "request", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Answer> answers;

    @OneToOne
    @JoinColumn(name = "reader_id")
    @JsonManagedReference
    private Reader reader;

    @OneToMany(mappedBy = "request", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Interview> interviews;
}
