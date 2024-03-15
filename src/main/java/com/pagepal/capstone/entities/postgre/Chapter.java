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

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CHAPTER")
@Where(clause = "status = 'ACTIVE'")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "chapter_number")
    private Integer chapterNumber;

    @Column(name = "pages")
    private Long pages;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @ManyToOne
    @JsonManagedReference
    private Book book;

    @OneToMany(mappedBy = "chapter", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Service> services;
}
