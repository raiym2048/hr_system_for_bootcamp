package com.example.hr_system.dto.message;

import com.example.hr_system.dto.file.FileResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class UserMessagesOneResponse {
        private Long userId;
        private String firstname;
        private String lastname;
        private String companyName;
        private String position;
        private Integer unreadCount;
        private FileResponse fileResponse;
        private List<MessageResponse> messageResponses;
}
