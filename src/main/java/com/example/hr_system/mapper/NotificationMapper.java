package com.example.hr_system.mapper;

import com.example.hr_system.dto.notification.NotificationResponse;
import com.example.hr_system.entities.Notification;

import java.util.List;

public interface NotificationMapper {

    NotificationResponse toDto(Notification notification);

    List<NotificationResponse> toDtos(List<Notification> responseList);
}
