package com.example.hr_system.service.impl;

import com.example.hr_system.entities.Notification;
import com.example.hr_system.entities.User;
import com.example.hr_system.enums.Role;
import com.example.hr_system.mapper.NotificationMapper;
import com.example.hr_system.repository.JobSeekerRepository;
import com.example.hr_system.repository.NotificationRepository;
import com.example.hr_system.repository.UserRepository;
import com.example.hr_system.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public Notification createNotificationStorage(Notification notificationStorage) {
        notificationStorage.setIsRead(false);
        notificationStorage.setIsRead(false);
        return notificationRepository.save(notificationStorage);
    }

    @Override
    public void sendNotificationToAllUsers(String content) {
        List<User> users = userRepository.findEmployersAndJobSeekers(
                List.of(Role.EMPLOYER, Role.JOB_SEEKER));

        for (User user : users ) {
            notificationMapper.toDtos(user.getNotification());
        }
    }

    @Override
    public List<Notification> getNotifs(Long userID) {
        return null;
    }

    @Override
    public Integer countUnreadNotificationsForUser(Long userId) {
        return notificationRepository.countUnreadNotifications(userId);
    }
}
