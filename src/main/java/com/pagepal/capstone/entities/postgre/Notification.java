package com.pagepal.capstone.entities.postgre;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pagepal.capstone.enums.NotificationEnum;
import com.pagepal.capstone.enums.NotificationRoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "NOTIFICATIONS")
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "status = 'ACTIVE'")
public class Notification {
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

    @Column(name= "title")
    private String title;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private NotificationEnum status;

    @Column(name = "notification_role")
    @Enumerated(EnumType.STRING)
    private NotificationRoleEnum notificationRole;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonManagedReference
    private Account account;
}
