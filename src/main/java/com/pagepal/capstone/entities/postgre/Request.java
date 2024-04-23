package com.pagepal.capstone.entities.postgre;

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

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "interview_at")
    private Date interviewAt;

    @Column(name = "meeting_code")
    private String meetingCode;

    @Column(name = "meeting_password")
    private String meetingPassword;

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

    @ManyToOne
    @JoinColumn(name = "reader_id")
    @JsonManagedReference
    private Reader reader;
}
