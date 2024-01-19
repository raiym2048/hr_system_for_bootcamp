package com.example.hr_system.dto.notification;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private Long userId;
    private String title;
    private Long imageId;
    private String arrivedDate;
    private String content;
}
