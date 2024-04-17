package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findAll(Pageable pageable);
    Page<Notification> findAllByAccountId(UUID accountId, Pageable pageable);
}
