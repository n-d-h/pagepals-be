package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Notification;
import com.pagepal.capstone.enums.NotificationRoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findAll(Pageable pageable);
    Page<Notification> findAllByAccountIdAndNotificationRole(UUID accountId, Pageable pageable, NotificationRoleEnum notificationRole);
    List<Notification> findAllByAccountId(UUID accountId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.account.id = :accountId AND n.isRead = false")
    Integer countUnreadByAccountId(UUID accountId);
}
