package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.notification.ListNotificationDto;
import com.pagepal.capstone.dtos.notification.NotificationCreateDto;
import com.pagepal.capstone.dtos.notification.NotificationDto;
import com.pagepal.capstone.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @QueryMapping("getAllNotifications")
    public ListNotificationDto getNotifications(
            @Argument("page") Integer page,
            @Argument(name = "pageSize") Integer pageSize,
            @Argument(name = "sort") String sort) {
        return notificationService.getNotifications(page, pageSize, sort);
    }

    @QueryMapping("getNotificationById")
    public NotificationDto getNotificationById(@Argument("id") UUID id) {
        return notificationService.getNotificationById(id);
    }

    @QueryMapping("getAllNotificationsByAccountId")
    public ListNotificationDto getNotificationsByAccountId(
            @Argument("accountId") UUID accountId,
            @Argument("page") Integer page,
            @Argument("pageSize") Integer pageSize,
            @Argument("sort") String sort
    ) {
        return notificationService.getNotificationsByAccountId(accountId, page, pageSize, sort);
    }

    @MutationMapping("createNotification")
    public NotificationDto createNotification(
            @Argument("notification") NotificationCreateDto notificationCreateDto
    ) {
        return notificationService.createNotification(notificationCreateDto);
    }

    @MutationMapping("readNotification")
    public NotificationDto updateNotification(
            @Argument("id") UUID id
    ) {
        return notificationService.updateNotification(id);
    }

    @MutationMapping("readAllNotifications")
    public List<NotificationDto> readAllNotification(
            @Argument("accountId") UUID accountId
    ) {
        return notificationService.readAllNotification(accountId);
    }
}
