package com.example.hr_system.dto.userMessages;

import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.dto.message.MessageResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserMessagesResponse {
    private Long userId;
    private String firstname;
    private String lastname;
    private String partnerEmail;
    private String companyName;
    private String position;
    private Integer unreadCount;
    private String fileResponse;
    private List<MessageResponse> messageResponses;
}
