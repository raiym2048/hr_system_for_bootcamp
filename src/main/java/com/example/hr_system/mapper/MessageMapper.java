package com.example.hr_system.mapper;

import com.example.hr_system.dto.message.MessageResponse;
import com.example.hr_system.dto.message.UserMessagesOneResponse;
import com.example.hr_system.entities.Message;
import com.example.hr_system.entities.User;

import java.util.List;

public interface MessageMapper {
    Object toDto(Message message);

    MessageResponse toDto(Message message, Boolean author);

    MessageResponse toDto2(Message message, Boolean author);

    List<MessageResponse> toDtos(List<Message> messages);

    List<MessageResponse> toDtos(List<Message> messages, User user, User user1);

    UserMessagesOneResponse userMessageOneDto(Message message, boolean b);

    UserMessagesOneResponse confirmMessage(Message message, boolean equals);
}
