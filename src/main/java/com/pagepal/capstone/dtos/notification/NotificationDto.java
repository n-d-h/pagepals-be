package com.pagepal.capstone.dtos.notification;

import com.pagepal.capstone.entities.postgre.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private UUID id;
    private String content;
    private String status;
    private Boolean isRead;
    private String createdAt;
    private String updatedAt;
    private Account account;
}
