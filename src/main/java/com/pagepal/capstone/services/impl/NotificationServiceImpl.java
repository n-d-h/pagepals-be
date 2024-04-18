package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.notification.ListNotificationDto;
import com.pagepal.capstone.dtos.notification.NotificationCreateDto;
import com.pagepal.capstone.dtos.notification.NotificationDto;
import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.Notification;
import com.pagepal.capstone.enums.NotificationEnum;
import com.pagepal.capstone.repositories.AccountRepository;
import com.pagepal.capstone.repositories.NotificationRepository;
import com.pagepal.capstone.services.NotificationService;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final AccountRepository accountRepository;
    private final DateUtils dateUtils;

    @Override
    public NotificationDto createNotification(NotificationCreateDto notificationCreateDto) {
        Account account = accountRepository.findById(notificationCreateDto.getAccountId()).orElse(null);

        if(account != null) {
            Notification notification = new Notification();
            notification.setContent(notificationCreateDto.getContent());
            notification.setStatus(NotificationEnum.ACTIVE);
            notification.setIsRead(Boolean.FALSE);
            notification.setAccount(account);
            notification.setCreatedAt(dateUtils.getCurrentVietnamDate());
            notification.setUpdatedAt(dateUtils.getCurrentVietnamDate());
            Notification noti = notificationRepository.save(notification);
            return NotificationDto.builder()
                    .id(noti.getId())
                    .content(noti.getContent())
                    .status(String.valueOf(noti.getStatus()))
                    .isRead(noti.getIsRead())
                    .createdAt(String.valueOf(noti.getCreatedAt()))
                    .updatedAt(String.valueOf(noti.getUpdatedAt()))
                    .account(noti.getAccount())
                    .build();
        }
        return null;
    }

    @Override
    public ListNotificationDto getNotifications(Integer page, Integer pageSize, String sort) {
        Pageable pageable = createPageable(page, pageSize, sort);
        Page<Notification> notifications = notificationRepository.findAll(pageable);
        return mapToNotificationDto(notifications);
    }

    @Override
    public NotificationDto getNotificationById(UUID id) {
        var notification = notificationRepository.findById(id).orElse(null);
        return notification == null ? null : NotificationDto.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .status(String.valueOf(notification.getStatus()))
                .isRead(notification.getIsRead())
                .createdAt(String.valueOf(notification.getCreatedAt()))
                .updatedAt(String.valueOf(notification.getUpdatedAt()))
                .account(notification.getAccount())
                .build();
    }

    @Override
    public ListNotificationDto getNotificationsByAccountId(UUID accountId, Integer page, Integer pageSize, String sort) {
        Pageable pageable = createPageable(page, pageSize, sort);
        Page<Notification> notifications = notificationRepository.findAllByAccountId(accountId, pageable);
        return mapToNotificationDto(notifications);
    }

    @Override
    public NotificationDto updateNotification(UUID id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification != null) {
            notification.setIsRead(Boolean.TRUE);
            notification.setUpdatedAt(dateUtils.getCurrentVietnamDate());
            Notification noti = notificationRepository.save(notification);
            return NotificationDto.builder()
                    .id(noti.getId())
                    .content(noti.getContent())
                    .status(String.valueOf(noti.getStatus()))
                    .isRead(noti.getIsRead())
                    .createdAt(String.valueOf(noti.getCreatedAt()))
                    .updatedAt(String.valueOf(noti.getUpdatedAt()))
                    .account(noti.getAccount())
                    .build();
        }
        return null;
    }

    private Pageable createPageable(Integer page, Integer pageSize, String sort) {
        if (page == null || page < 0)
            page = 0;

        if (pageSize == null || pageSize < 0)
            pageSize = 10;

        if (sort != null && sort.equals("desc")) {
            return PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        } else {
            return PageRequest.of(page, pageSize, Sort.by("createdAt").ascending());
        }
    }

    private ListNotificationDto mapToNotificationDto(Page<Notification> notifications) {
        var listNotificationDtos = new ListNotificationDto();

        if (notifications == null) {
            listNotificationDtos.setList(Collections.emptyList());
            listNotificationDtos.setPagination(null);
            return listNotificationDtos;
        } else {
            var pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(notifications.getTotalPages());
            pagingDto.setTotalOfElements(notifications.getTotalElements());
            pagingDto.setSort(notifications.getSort().toString());
            pagingDto.setCurrentPage(notifications.getNumber());
            pagingDto.setPageSize(notifications.getSize());

            listNotificationDtos.setList(notifications.map(notification -> NotificationDto.builder()
                    .id(notification.getId())
                    .content(notification.getContent())
                    .status(String.valueOf(notification.getStatus()))
                    .isRead(notification.getIsRead())
                    .createdAt(String.valueOf(notification.getCreatedAt()))
                    .updatedAt(String.valueOf(notification.getUpdatedAt()))
                    .account(notification.getAccount())
                    .build()).toList());
            listNotificationDtos.setPagination(pagingDto);
            return listNotificationDtos;
        }
    }
}
