package com.example.hr_system.service.impl;

import com.example.hr_system.config.MyHandler;
import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.dto.message.MessageResponse;
import com.example.hr_system.dto.userMessages.UserMessagesResponse;
import com.example.hr_system.entities.*;
import com.example.hr_system.enums.Role;
import com.example.hr_system.mapper.FileMapper;
import com.example.hr_system.mapper.MessageMapper;
import com.example.hr_system.mapper.UserMessageMapper;
import com.example.hr_system.repository.*;
import com.example.hr_system.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final FileMapper fileMapper;
    private final MessageMapper messageMapper;
    private final SenderRecipientHistoryRepository senderRecipientHistoryRepository;
    private final FileRepository fileRepository;



    @Override
    public User getUsernameFromToken(String token) {
        String[] chunks = token.substring(7).split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        JSONParser jsonParser = new JSONParser();
        JSONObject object = null;
        try {
            object = (JSONObject) jsonParser.parse(decoder.decode(chunks[1]));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return userRepository.findByEmail(String.valueOf(object.get("sub"))).orElseThrow(() -> new RuntimeException("user can be null"));
    }

    @Override
    public Integer getAllUsersUnReadMessagesCount(String token) {
        User user = getUsernameFromToken(token);
        List<SenderRecipientHistory> senderRecipientHistory = senderRecipientHistoryRepository.findAllByRecipientEmailOrSenderEmail(user.getEmail(), user.getEmail());
        int count  = 0;
        for (SenderRecipientHistory senderRecipientHistory1: senderRecipientHistory){
            if (senderRecipientHistory1.getSenderEmail().equals(user.getEmail())){
                count+= senderRecipientHistory1.getSenderUnreadCount()==null?0:senderRecipientHistory1.getSenderUnreadCount();
            }
            else {
                count+= senderRecipientHistory1.getRecipientUnreadCount()==null?0:senderRecipientHistory1.getRecipientUnreadCount();
            }

        }
        return count;
    }
    @Override
    public Integer getAllUsersUnReadMessagesCount(User user) {
        List<Message> messages = messageRepository.findAllByRecipient(user.getEmail());
        Integer count = 0 ;
        for (Message message: messages){
            count += message.isRead()?1:0;
        }
        return count;
    }
    @Override
    public List<UserMessagesResponse>  getAllHistoryChat(String token) {
        User sender = getUsernameFromToken(token);
        return listSendMessageHistory(sender);
    }
    @Override
    public List<MessageResponse> getAllUnreadMessages(String token) {
        User user = getUsernameFromToken(token);
        List<Message> messages = messageRepository.findByRecipientAndIsReadOrderByTimeDesc(user.getEmail(), false);
        return messageMapper.toDtos(messages);
    }

    @Override
    public UserMessagesResponse openUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        UserMessagesResponse userMessagesResponse = new UserMessagesResponse();
        userMessagesResponse.setUserId(user.getId());
        userMessagesResponse.setPartnerEmail(user.getEmail());
        String url_path = setFileResponseEmployerOrJobSeeker(user);
        System.out.println("The url_path is: "+url_path);
        userMessagesResponse.setFileResponse(url_path);
        userMessagesResponse.setPosition(getPositionEmployerOrJobSeeker(user));
        if (user.getRole().equals(Role.JOB_SEEKER)){
            userMessagesResponse.setFirstname(user.getJobSeeker().getFirstname());
            userMessagesResponse.setLastname(user.getJobSeeker().getLastname());
        }
        if (user.getRole().equals(Role.EMPLOYER)){
            userMessagesResponse.setCompanyName(user.getEmployer().getCompanyName());
        }
        userMessagesResponse.setFileResponse(setFileResponseEmployerOrJobSeeker(user));
        return userMessagesResponse;
    }

    private List<UserMessagesResponse> listSendMessageHistory(User user) {
        List<UserMessagesResponse> messageResponses = new ArrayList<>();

        List<SenderRecipientHistory> senderRecipientHistories =
                senderRecipientHistoryRepository.findAllBySenderEmailOrRecipientEmail(user.getEmail(), user.getEmail());

        for (SenderRecipientHistory senderRecipientHistory : senderRecipientHistories) {

            User user1 = userRepository.findById(setUserId(user.getEmail(), senderRecipientHistory)).get();
            System.out.println("the email is1121:  "+user1.getEmail());

            UserMessagesResponse userMessagesResponse = new UserMessagesResponse();
            userMessagesResponse.setUserId(user1.getId());
            userMessagesResponse.setPartnerEmail(user1.getEmail());
            String url_path = setFileResponseEmployerOrJobSeeker(user);
            System.out.println("The url_path is: "+url_path);
            userMessagesResponse.setFileResponse(url_path);
            userMessagesResponse.setPosition(getPositionEmployerOrJobSeeker(user1));
            if (user1.getRole().equals(Role.JOB_SEEKER)){
                userMessagesResponse.setFirstname(user1.getJobSeeker().getFirstname());
                userMessagesResponse.setLastname(user1.getJobSeeker().getLastname());
            }
            if (user1.getRole().equals(Role.EMPLOYER)){
                userMessagesResponse.setCompanyName(user1.getEmployer().getCompanyName());
            }
            userMessagesResponse.setFileResponse(setFileResponseEmployerOrJobSeeker(user1));
            List<Message> messages = messageRepository.findByIdInOrderByTimeDesc(senderRecipientHistory.getMessageIds());
            userMessagesResponse.setMessageResponses(messageMapper.toDtos(messages,user, user1));
            System.out.println(user.getEmail() + " the recipient: " + user1.getEmail());
            //if (senderRecipientHistory1.getSenderEmail().equals(user.getEmail())){
            //                count+= senderRecipientHistory1.getSenderUnreadCount()==null?0:senderRecipientHistory1.getSenderUnreadCount();
            //            }
            //            else {
            //                count+= senderRecipientHistory1.getRecipientUnreadCount()==null?0:senderRecipientHistory1.getRecipientUnreadCount();
            //            }
            if (senderRecipientHistory.getSenderEmail().equals(user.getEmail()))
                userMessagesResponse.setUnreadCount(senderRecipientHistory.getSenderUnreadCount());
            else if(senderRecipientHistory.getRecipientEmail().equals(user.getEmail())){
                userMessagesResponse.setUnreadCount(senderRecipientHistory.getRecipientUnreadCount());
            }
            else {
                userMessagesResponse.setUnreadCount(0);

            }
            messageResponses.add(userMessagesResponse);
        }

        return messageResponses;
    }

    private Integer getUsersUnreadCountFromSecondUser(User user, User user1) {
        List<Message> messages = messageRepository.findBySenderAndRecipient(user1.getEmail(), user.getEmail());
        Integer count = 0 ;
        for (Message message: messages){
            count += !message.isRead()?1:0;
        }
        return count;
    }

    private Long setUserId(String email, SenderRecipientHistory senderRecipientHistory) {
        if (email.equals(senderRecipientHistory.getSenderEmail()))
            return userRepository.findByEmail(senderRecipientHistory.getRecipientEmail()).get().getId();
        return userRepository.findByEmail(senderRecipientHistory.getSenderEmail()).get().getId();
    }


    private String setFileResponseEmployerOrJobSeeker(User user1) {
        if(user1.getRole().equals(Role.JOB_SEEKER)){
            if (user1.getJobSeeker().getImage()!=null) {

                Optional<FileData> fileData = fileRepository.findById(user1.getJobSeeker().getImage().getId());
                if (fileData.isPresent())
                    System.out.println("the path: " + fileData.get().getPath());
                return user1.getJobSeeker().getImage() != null ? fileMapper.toDto(
                        fileRepository.findById(user1.getJobSeeker().getImage().getId()).get()
                ).getPath() : null;
            }
        }
        else if(user1.getRole().equals(Role.EMPLOYER)) {
            if (user1.getEmployer().getResume()!=null){
                Optional<FileData> fileData = fileRepository.findById(user1.getEmployer().getResume().getId());
                if (fileData.isPresent())
                    System.out.println("the path: "+fileData.get().getPath());
                return user1.getEmployer().getResume()!=null?fileMapper.toDto(
                        fileRepository.findById(user1.getEmployer().getResume().getId()).get()).getPath():null;

            }


        }
        return null;
        }

    private String getPositionEmployerOrJobSeeker(User user1) {
        if (user1.getRole().equals(Role.JOB_SEEKER)){
            JobSeeker jobSeeker = user1.getJobSeeker();
            if(jobSeeker.getProfessions().size()>0){
                return jobSeeker.getProfessions().get(0).getPosition().getName();
            }
        }
        return null;
    }
}
