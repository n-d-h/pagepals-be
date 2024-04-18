package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.notification.ListNotificationDto;
import com.pagepal.capstone.dtos.notification.NotificationCreateDto;
import com.pagepal.capstone.dtos.notification.NotificationDto;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    NotificationDto createNotification(NotificationCreateDto notificationCreateDto);
    ListNotificationDto getNotifications(Integer page, Integer pageSize, String sort);
    NotificationDto getNotificationById(UUID id);
    ListNotificationDto getNotificationsByAccountId(UUID accountId, Integer page, Integer pageSize, String sort);
    NotificationDto updateNotification(UUID id);
    List<NotificationDto> readAllNotification(UUID accountId);
}
