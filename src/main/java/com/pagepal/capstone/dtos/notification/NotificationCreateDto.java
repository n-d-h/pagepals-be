package com.pagepal.capstone.dtos.notification;

import com.pagepal.capstone.enums.NotificationRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateDto {
    private String title;
    private String content;
    private NotificationRoleEnum notificationRole;
    private UUID accountId;
}
