package com.pagepal.capstone.dtos.notification;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListNotificationDto {
    private List<NotificationDto> list;
    private PagingDto pagination;
}
