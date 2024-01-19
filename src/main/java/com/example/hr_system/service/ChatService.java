package com.example.hr_system.service;

import com.example.hr_system.dto.message.MessageResponse;
import com.example.hr_system.dto.message.TypeResponse;
import com.example.hr_system.dto.userMessages.UserMessagesResponse;
import com.example.hr_system.entities.Message;
import com.example.hr_system.entities.User;
import com.example.hr_system.entities.UserMessageInfo;
import com.example.hr_system.entities.UserMessages;

import java.util.List;

public interface ChatService {
    User getUsernameFromToken(String token);


    Integer getAllUsersUnReadMessagesCount(String token);

    Integer getAllUsersUnReadMessagesCount(User user);

    List<UserMessagesResponse> getAllHistoryChat(String token);

    List<MessageResponse> getAllUnreadMessages(String token);

    UserMessagesResponse openUserById(Long userId);
}
