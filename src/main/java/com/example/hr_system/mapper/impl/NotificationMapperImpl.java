package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.notification.NotificationResponse;
import com.example.hr_system.entities.Notification;
import com.example.hr_system.mapper.FileMapper;
import com.example.hr_system.mapper.NotificationMapper;
import com.example.hr_system.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationMapperImpl implements NotificationMapper {
    private final FileMapper fileMapper;
    private final FileRepository fileRepository;
    @Override
    public NotificationResponse toDto(Notification notification) {
        if (notification == null){
            return null;
        }

        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setUserId(
                notification.getUser()!=null?
                notification.getUser().getId():null);
        response.setTitle(notification.getTitle());
        response.setImageId(notification.getImageId());
        response.setArrivedDate(notification.getArrivedDate());
        response.setContent(notification.getContent());
        return response;
    }

    @Override
    public List<NotificationResponse> toDtos(List<Notification> responseList) {
        List<NotificationResponse> notificationResponses = new ArrayList<>();
        for (Notification notification :responseList) {
            notificationResponses.add(toDto(notification));
        }
        return notificationResponses;
    }
}
