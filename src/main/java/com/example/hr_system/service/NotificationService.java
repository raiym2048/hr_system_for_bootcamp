package com.example.hr_system.service;

import com.example.hr_system.entities.Notification;

import java.util.List;

public interface NotificationService {

      Notification createNotificationStorage(Notification notificationStorage);

      void sendNotificationToAllUsers(String content);

     List<Notification> getNotifs(Long userID);

    Integer countUnreadNotificationsForUser(Long userId);
}
