package com.pagepal.capstone.entities.postgre;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pagepal.capstone.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QUESTION")
@Where(clause = "status = 'ACTIVE'")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonManagedReference
    private Question question;

    @ManyToOne
    @JoinColumn(name = "request_id")
    @JsonManagedReference
    private Request request;
}
