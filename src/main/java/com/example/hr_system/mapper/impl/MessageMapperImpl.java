package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.dto.message.MessageResponse;
import com.example.hr_system.dto.message.NewStatus;
import com.example.hr_system.dto.message.UnreadMessages;
import com.example.hr_system.dto.message.UserMessagesOneResponse;
import com.example.hr_system.entities.Message;
import com.example.hr_system.entities.SenderRecipientHistory;
import com.example.hr_system.entities.User;
import com.example.hr_system.enums.Role;
import com.example.hr_system.mapper.FileMapper;
import com.example.hr_system.mapper.MessageMapper;
import com.example.hr_system.repository.FileRepository;
import com.example.hr_system.repository.SenderRecipientHistoryRepository;
import com.example.hr_system.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class MessageMapperImpl implements MessageMapper {
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final UserRepository userRepository;
    private final SenderRecipientHistoryRepository senderRecipientHistoryRepository;


    @Override
    public MessageResponse toDto(Message message) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setSentTime(message.getTime().toString());
        messageResponse.setRead(message.isRead());
        messageResponse.setContent(message.getContent());
        messageResponse.setMessageId(message.getId());
        messageResponse.setRecipient(message.getRecipient());
        messageResponse.setAmIAuthor(message.getAmIAuthor());
        List<FileResponse> fileResponses = new ArrayList<>();

        if (message.getFileIds()!=null) {
            for (Long fileId : message.getFileIds()) {
                FileResponse fileResponse = fileMapper.toDto(fileRepository.findById(fileId).get());
                fileResponses.add(fileResponse);
            }
            messageResponse.setFileResponse(fileResponses);

        }

        messageResponse.setSender(message.getSender());
        UnreadMessages unreadMessages = new UnreadMessages();
        unreadMessages.setUnreadCount(message.getUnreadCountMessages());
        NewStatus newStatus = new NewStatus();
        newStatus.setStatus("read");
        return messageResponse;
    }
    @Override
    public MessageResponse toDto(Message message, Boolean author) {
        MessageResponse messageResponse = new MessageResponse();


        messageResponse.setMessageId(message.getId());
        messageResponse.setScribeId(message.getScribeId());
        messageResponse.setSentTime(message.getTime().toString());
        messageResponse.setRead(message.isRead());
        messageResponse.setContent(message.getContent());
        messageResponse.setMessageId(message.getId());
        messageResponse.setRecipient(message.getRecipient());
        List<FileResponse> fileResponses = new ArrayList<>();

        if (message.getFileIds()!=null) {
            for (Long fileId : message.getFileIds()) {
                FileResponse fileResponse = fileMapper.toDto(fileRepository.findById(fileId).get());
                fileResponses.add(fileResponse);
            }
            messageResponse.setFileResponse(fileResponses);

        }        messageResponse.setSender(message.getSender());
        UnreadMessages unreadMessages = new UnreadMessages();
        unreadMessages.setUnreadCount(message.getUnreadCountMessages());
        NewStatus newStatus = new NewStatus();
        newStatus.setStatus("read");
        messageResponse.setAmIAuthor(author);
        return messageResponse;
    }
    @Override
    public MessageResponse toDto2(Message message, Boolean author) {
        MessageResponse messageResponse = new MessageResponse();


        messageResponse.setMessageId(message.getId());
        messageResponse.setSentTime(message.getTime().toString());
        messageResponse.setRead(message.isRead());
        messageResponse.setContent(message.getContent());
        messageResponse.setMessageId(message.getId());
        messageResponse.setRecipient(message.getRecipient());
        List<FileResponse> fileResponses = new ArrayList<>();

        if (message.getFileIds()!=null) {
            for (Long fileId : message.getFileIds()) {
                FileResponse fileResponse = fileMapper.toDto(fileRepository.findById(fileId).get());
                fileResponses.add(fileResponse);
            }
            messageResponse.setFileResponse(fileResponses);

        }        messageResponse.setSender(message.getSender());
        messageResponse.setSender(message.getSender());
        UnreadMessages unreadMessages = new UnreadMessages();
        unreadMessages.setUnreadCount(message.getUnreadCountMessages());
        NewStatus newStatus = new NewStatus();
        newStatus.setStatus("read");
        messageResponse.setAmIAuthor(author);
        return messageResponse;
    }


    @Override
    public List<MessageResponse> toDtos(List<Message> messages) {
        List<MessageResponse> m = new ArrayList<>();
        for (Message message: messages){
            m.add(toDto(message));
        }
        return m;
    }
    @Override
    public List<MessageResponse> toDtos(List<Message> messages, User user, User user1) {
        List<MessageResponse> m = new ArrayList<>();
        for (Message message: messages){
            m.add(toDto(message, message.getSender().equals(user.getEmail())));
        }
        return m;
    }

    @Override
    public UserMessagesOneResponse userMessageOneDto(Message message, boolean author) {
        UserMessagesOneResponse messageResponse = new UserMessagesOneResponse();
        User sender = userRepository.findByEmail(message.getSender()).get();
        User recipient = userRepository.findByEmail(message.getRecipient()).get();
        User senderUser = author? userRepository.findByEmail(message.getSender()).get(): userRepository.findByEmail(message.getRecipient()).get();
        Boolean isJobSeeker = senderUser.getRole().equals(Role.JOB_SEEKER);
        if (recipient.getRole().equals(Role.JOB_SEEKER)){
            if (recipient.getJobSeeker().getImage()!=null)
                messageResponse.setFileResponse(fileMapper.toDto(fileRepository.findById(recipient.getJobSeeker().getImage().getId()).get()));
            messageResponse.setFirstname(recipient.getJobSeeker().getFirstname());
            messageResponse.setLastname(recipient.getJobSeeker().getLastname());
            if (recipient.getJobSeeker().getProfessions().size()>0){
                messageResponse.setPosition(recipient.getJobSeeker().getProfessions().get(0).getPosition().getName());
            }
        }
        else {
            if (recipient.getEmployer().getResume()!=null)
                messageResponse.setFileResponse(fileMapper.toDto(fileRepository.findById(recipient.getEmployer().getResume().getId()).get()));
            messageResponse.setCompanyName(recipient.getEmployer().getCompanyName());
        }
        Optional<SenderRecipientHistory> senderRecipientHistory = senderRecipientHistoryRepository.findBySenderEmailAndRecipientEmail(message.getSender(), message.getRecipient());
        if (senderRecipientHistory.isPresent()){
            messageResponse.setUnreadCount(senderRecipientHistory.get().getRecipientUnreadCount());
        }else {
            messageResponse.setUnreadCount(0);

        }

        MessageResponse messageResponse1 = toDto2(message, author);
        messageResponse.setUserId(sender.getId());
        List<MessageResponse> messageResponses = new ArrayList<>();
        messageResponses.add(messageResponse1);
        messageResponse.setMessageResponses(messageResponses);

        return messageResponse;
    }

    @Override
    public UserMessagesOneResponse confirmMessage(Message message, boolean equals) {
        User recipient  = userRepository.findByEmail(message.getRecipient()).get();
        UserMessagesOneResponse userMessagesOneResponse = new UserMessagesOneResponse();
        userMessagesOneResponse.setUserId(recipient.getId());
        if (recipient.getRole().equals(Role.JOB_SEEKER)){
            userMessagesOneResponse.setFirstname(recipient.getFirstname());
            userMessagesOneResponse.setLastname(recipient.getLastname());
            if (recipient.getJobSeeker().getImage()!=null){
                userMessagesOneResponse.setFileResponse(fileMapper.toDto(fileRepository.findById(recipient.getJobSeeker().getImage().getId()).get()));
            }
        }
        if (recipient.getRole().equals(Role.EMPLOYER)){
            userMessagesOneResponse.setCompanyName(recipient.getEmployer().getCompanyName());
            if (recipient.getEmployer().getResume()!=null){
                userMessagesOneResponse.setFileResponse(fileMapper.toDto(fileRepository.findById(recipient.getEmployer().getResume().getId()).get()));
            }
        }

      
            userMessagesOneResponse.setUnreadCount(0);



        MessageResponse messageResponse1 = toDto2(message, equals);
        List<MessageResponse> messageResponses = new ArrayList<>();
        messageResponses.add(messageResponse1);
        userMessagesOneResponse.setMessageResponses(messageResponses);
        return userMessagesOneResponse;
    }


}
