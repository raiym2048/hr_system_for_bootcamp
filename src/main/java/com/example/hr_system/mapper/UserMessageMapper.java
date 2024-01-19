package com.example.hr_system.mapper;

import com.example.hr_system.dto.message.MessageResponse;
import com.example.hr_system.entities.Message;
import com.example.hr_system.entities.UserMessages;

import java.util.List;

public interface UserMessageMapper {
    List<MessageResponse> toDtoS(List<Message> messages);

    MessageResponse toDto(Message message);
}
