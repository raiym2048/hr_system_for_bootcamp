package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.dto.message.MessageResponse;
import com.example.hr_system.entities.Message;
import com.example.hr_system.entities.UserMessages;
import com.example.hr_system.mapper.FileMapper;
import com.example.hr_system.mapper.UserMessageMapper;
import com.example.hr_system.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class UserMessageMapperImpl implements UserMessageMapper {
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;


    @Override
    public List<MessageResponse> toDtoS(List<Message> messages) {
        List<MessageResponse> messageResponses = new ArrayList<>();
        for(Message userMessages1: messages){
            messageResponses.add(toDto(userMessages1));
        }
        return messageResponses;
    }

    @Override
    public MessageResponse toDto(Message message) {
        MessageResponse messageResponse = new MessageResponse();
        List<FileResponse> fileResponses = new ArrayList<>();

        if (message.getFileIds()!=null) {
            for (Long fileId : message.getFileIds()) {
                FileResponse fileResponse = fileMapper.toDto(fileRepository.findById(fileId).get());
                fileResponses.add(fileResponse);
            }
            messageResponse.setFileResponse(fileResponses);

        }        messageResponse.setSender(message.getSender());
        messageResponse.setMessageId(message.getId());
        messageResponse.setRead(message.isRead());
        messageResponse.setSender(message.getSender());
        messageResponse.setRecipient(message.getRecipient());
        messageResponse.setContent(message.getContent());
        messageResponse.setSentTime(message.getTime().toString());
        return messageResponse;
    }
}
