package com.example.hr_system.dto.notificationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationCountResponse {
    Integer unreadCount;
    Object responseEntity;
}
