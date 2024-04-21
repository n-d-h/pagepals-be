package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.notification.ListNotificationDto;
import com.pagepal.capstone.dtos.notification.NotificationCreateDto;
import com.pagepal.capstone.dtos.notification.NotificationDto;
import com.pagepal.capstone.enums.NotificationRoleEnum;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    NotificationDto createNotification(NotificationCreateDto notificationCreateDto);
    ListNotificationDto getNotifications(Integer page, Integer pageSize, String sort);
    NotificationDto getNotificationById(UUID id);
    ListNotificationDto getNotificationsByAccountId(UUID accountId, Integer page, Integer pageSize, String sort, NotificationRoleEnum notificationRole);
    NotificationDto updateNotification(UUID id);
    List<NotificationDto> readAllNotification(UUID accountId);
}
