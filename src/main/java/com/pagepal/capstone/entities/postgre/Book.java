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
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BOOK")
@Where(clause = "status = 'ACTIVE'")
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "title")
    private String title;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "published_date")
    private String publishedDate;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "small_thumbnail_url")
    private String smallThumbnailUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "language")
    private String language;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Service> services;
}
