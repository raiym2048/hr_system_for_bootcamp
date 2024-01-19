package com.example.hr_system.config;


import com.example.hr_system.dto.message.*;
import com.example.hr_system.dto.notificationDto.NotificationCountResponse;
import com.example.hr_system.dto.userMessages.UserMessagesResponse;
import com.example.hr_system.entities.*;
import com.example.hr_system.enums.Role;
import com.example.hr_system.mapper.FileMapper;
import com.example.hr_system.mapper.MessageMapper;
import com.example.hr_system.mapper.NotificationMapper;
import com.example.hr_system.mapper.UserMessageMapper;
import com.example.hr_system.repository.*;
import com.example.hr_system.service.BlockedUserService;
import com.example.hr_system.service.ChatService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class MyHandler implements WebSocketHandler{
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    @Autowired
    private  MessageRepository messageRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  UserMessageInfoRepository userMessageInfoRepository;
    @Autowired
    private  UserMessagesRepository userMessagesRepository;
    @Autowired
    private  EmployerRepository employerRepository;
    @Autowired
    private  BlockedUserService blockedUserService;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private SenderRecipientHistoryRepository senderRecipientHistoryRepository;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ChatService chatService;

    public void connectConfirm(User user, User user1){

        WebSocketSession session = userSessions.get(user1.getEmail());
        //userSessions.put(user.getEmail(),userSessions);


        ObjectMapper objectMapper1 = new ObjectMapper();
        objectMapper1.registerModule(new JavaTimeModule());

        if (session != null && session.isOpen()) {
            try {

                TypeResponse typeResponse = new TypeResponse();
                typeResponse.setType("statusUpdate");
                ConnectConfirmResponse connectConfirmResponse = new ConnectConfirmResponse();
                connectConfirmResponse.setPartnerId(user.getId());
                typeResponse.setResponseEntity(connectConfirmResponse);

                session.sendMessage(new TextMessage(objectMapper1.writeValueAsString(typeResponse)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (session!= null){

            }
            }
    }


    public void notificationSend(User user, Notification notification){

        WebSocketSession session = userSessions.get(user.getEmail());
        //userSessions.put(user.getEmail(),userSessions);


        ObjectMapper objectMapper1 = new ObjectMapper();
        objectMapper1.registerModule(new JavaTimeModule());

        if (session != null) {
            try {
                notification.setIsSend(true);
                notificationRepository.save(notification);
                NotificationCountResponse notificationCountResponse = new NotificationCountResponse();
                List<Notification> notificationList = notificationRepository.findAll().stream().filter(el -> !el.getIsSend()).toList();
                notificationCountResponse.setUnreadCount(notificationList.size());
                notificationCountResponse.setResponseEntity(notificationMapper.toDto(notification));
                TypeResponse typeResponse = new TypeResponse();
                typeResponse.setType("notification");
                typeResponse.setResponseEntity(notificationCountResponse);

                session.sendMessage(new TextMessage(objectMapper1.writeValueAsString(typeResponse)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            notification.setIsSend(false);
            notificationRepository.save(notification);

            System.out.println("No WebSocketSession found for user: " + user.getEmail());
        }
    }

    public void notificationSend2(User user, Notification notification){


        WebSocketSession session = userSessions.get(user.getEmail());
        //userSessions.put(user.getEmail(),userSessions);

        TypeResponse typeResponse = new TypeResponse();
        typeResponse.setType("notification");
        typeResponse.setResponseEntity(notificationMapper.toDto(notification));

        ObjectMapper objectMapper1 = new ObjectMapper();
        objectMapper1.registerModule(new JavaTimeModule());

        if (session != null) {
            try {
                notification.setIsSend(true);
                notificationRepository.save(notification);
                session.sendMessage(new TextMessage(objectMapper1.writeValueAsString(typeResponse)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            notification.setIsSend(false);
            notificationRepository.save(notification);
            List<Notification> notificationList = notificationRepository.findAll().stream().filter(el -> !el.getIsSend()).toList();
            TypeResponse typeResponseNotificationsIsNotSend = new TypeResponse();
            typeResponse.setType("notificationsIsNotSend");
            typeResponse.setResponseEntity(notificationList.size());

            ObjectMapper objectMapper2 = new ObjectMapper();
            objectMapper2.registerModule(new JavaTimeModule());

            try {
                session.sendMessage(new TextMessage(objectMapper2.writeValueAsString(typeResponseNotificationsIsNotSend)));
                System.out.println("sended!!!!!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("No WebSocketSession found for user: " + user.getEmail());
        }
    }



    @Transactional
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();

        System.out.println("Attributes of the session:");
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        // Здесь вы можете, например, извлечь токен или имя пользователя из параметров сессии и добавить их в вашу карту.
        Object senderObj = session.getAttributes().get("token");


        System.out.println("Setting sender attribute for session. Sender:111 " + senderObj);
        if (senderObj != null) {
            addSessionForUser(senderObj.toString(), session);
        }

        if(senderObj != null) {
            String sender = senderObj.toString();
            System.out.println("put112!"+blockedUserService.getUsernameFromToken(sender).getEmail());
            userSessions.put(blockedUserService.getUsernameFromToken(sender).getEmail(), session);
        }
        User user = blockedUserService.getUsernameFromToken(senderObj.toString());
        user.setOnline(true);
        userRepository.save(user);
        List<UserMessages> userMessages = userMessagesRepository.findByEmailOfRecipient(user.getEmail());
        Optional<UserMessageInfo> userMessageInfo = Optional.of(new UserMessageInfo());
        if (senderObj!=null)
            userMessageInfo = userMessageInfoRepository.findByEmail(user.getEmail());

        if (userMessageInfo.isEmpty()){
            UserMessageInfo userMessageInfo1 = new UserMessageInfo();
            userMessageInfo1.setEmail(user.getEmail());
            userMessageInfo1.setUnReadMessages(userMessageInfo1.getUnReadMessages()+1);
            if (userMessages.size()==0){
                UserMessages userMessages1 = new UserMessages();
                userMessages.add(userMessages1);
            }
            else {
                if (userMessages.size()> 0){
                    userMessages.get(0).setUserMessageInfo(userMessageInfo1);
                    userMessageInfoRepository.save(userMessageInfo1);
                }

            }
        }
        else {
            userMessageInfo.get().setEmail(senderObj.toString());
            userMessageInfo.get().setUnReadMessages(userMessageInfo.get().getUnReadMessages()+userMessages.get(0).getUnReadMessages());
            if (userMessages.size()==0){
                UserMessages userMessages1 = new UserMessages();
                userMessages1.setUserMessageInfo(userMessageInfo.get());
                userMessages.add(userMessages1);
            }
            else {
                userMessages.get(0).setUserMessageInfo(userMessageInfo.get());
                userMessageInfoRepository.save(userMessageInfo.get());
            }
        }
        // User user =  blockedUserService.getUsernameFromToken(token);


        System.out.println("\n\nConnection established from: " + session.getRemoteAddress());

//        String messageJson = new ObjectMapper().writeValueAsString(sendMessageHistory(user));
//        TextMessage textMessage = new TextMessage(messageJson);
//        session.sendMessage(textMessage);

    }



    @Transactional
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        // Deserialize the received message

        String payload = (String) webSocketMessage.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        System.out.println("Payload: " + payload);

        Message message = objectMapper.readValue(payload, Message.class);
        message.setTime(LocalDateTime.now());
        User userFromToken  = blockedUserService.getUsernameFromToken(session.getAttributes().get("token").toString());
        System.out.println("begin11");
        System.out.println(session.getRemoteAddress());

        // Вывести каждую пару ключ-значение
        for (Map.Entry<String, Object> entry : session.getAttributes().entrySet()) {

            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("end");


        Optional<User> recipient = userRepository.findByEmail(message.getRecipient());
        Optional<UserMessageInfo> userMessageInfo = userMessageInfoRepository.findByEmail(recipient.isPresent()? recipient.get().getEmail():null);
        System.out.println("the recipient:"+message.getRecipient());

        if (recipient.isEmpty()){
            throw new NotFoundException("the recipient with this email not found!");
        }



        // Override the sender from the session attributes
        Object senderObj = message.getSender();
        List<UserMessages> userMessagesList = userMessagesRepository.findBySenderEmail(senderObj.toString());
        UserMessages userMessages = userMessagesList.isEmpty() ? new UserMessages() : userMessagesList.get(0);

        if (senderObj != null) {
            String senderFromToken = senderObj.toString();
            message.setSender(senderFromToken); // Setting the sender from the token
        }
        else {
            throw new NotFoundException("sender is not available(null this token/email)");
        }

        //до сюда будет глава

        WebSocketSession recipientSession = userSessions.get(message.getRecipient());
        WebSocketSession senderSession = userSessions.get(message.getSender());

        Long messageId2 = messageRepository.save(message).getId();
        senderRecipientHistory(message.getSender(), message.getRecipient(), messageId2);


        if (recipientSession != null && recipientSession.isOpen()) {
            System.out.println(recipientSession.getAttributes());
            Optional<SenderRecipientHistory> senderRecipientHistory2 = senderRecipientHistoryRepository.findBySenderEmailAndRecipientEmail(message.getSender(), message.getRecipient());
            if (senderRecipientHistory2.isPresent()){
                System.out.println("log1) sender: " + senderRecipientHistory2.get().getSenderEmail() + ", recipient: " + senderRecipientHistory2.get().getRecipientEmail());
                senderRecipientHistory2.get().setSenderUnreadCount(0);
                senderRecipientHistoryRepository.save(senderRecipientHistory2.get());
            }


            userMessages.setLastActionDate(String.valueOf(LocalDateTime.now()));
                userMessages.setEmailOfRecipient(recipient.get().getEmail());
                userMessages.setUnReadMessages(0);
                if (recipient.get().getRole().equals(Role.JOB_SEEKER)){
                    if (employerRepository.findByEmail(senderObj.toString())!=null)
                        userMessages.setName(employerRepository.findByEmail(senderObj.toString()).getCompanyName());
                }


            userMessages.setUnReadMessages(0);
            UnreadMessages unreadMessages = new UnreadMessages();
            unreadMessages.setUnreadCount(userMessages.getUnReadMessages());
            TypeResponse typeResponse = new TypeResponse();
            typeResponse.setType("newMessage");
            message.setScribeId(userFromToken.getId());
            Long messageId = messageRepository.save(message).getId();
            message.setId(messageId);
            UserMessagesOneResponse userMessagesOneResponse = new UserMessagesOneResponse();
            userMessagesOneResponse = messageMapper.userMessageOneDto(message, !userFromToken.getEmail().equals(message.getSender()));

            typeResponse.setResponseEntity(userMessagesOneResponse);

            ObjectMapper objectMapper1 = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            // send for recipient
            recipientSession.sendMessage(new TextMessage(objectMapper1.writeValueAsString(typeResponse)));


            //send for sender
            //M E S S A G E   ---   C O N F I R M


        } else {
            System.out.println("else w12");

            Optional<SenderRecipientHistory> senderRecipientHistory = senderRecipientHistoryRepository.findBySenderEmailAndRecipientEmail(message.getSender(), message.getRecipient());
            Optional<SenderRecipientHistory> senderRecipientHistory2 = senderRecipientHistoryRepository.findBySenderEmailAndRecipientEmail(message.getRecipient(), message.getSender());
                if (senderRecipientHistory.isPresent()){
                    System.out.println("log2) sender: " + senderRecipientHistory.get().getSenderEmail() + ", recipient: " + senderRecipientHistory.get().getRecipientEmail());
                    if (senderRecipientHistory.get().getRecipientUnreadCount()!=null){
                        senderRecipientHistory.get().setRecipientUnreadCount(senderRecipientHistory.get().getRecipientUnreadCount()+1);
                        senderRecipientHistoryRepository.save(senderRecipientHistory.get());

                    }
                    else {
                        System.out.println("nor present!");
                        senderRecipientHistory.get().setRecipientUnreadCount(1);
                        senderRecipientHistoryRepository.save(senderRecipientHistory.get());
                    }
                }
                else if (senderRecipientHistory2.isPresent()){
                    System.out.println("log2) sender: " + senderRecipientHistory2.get().getSenderEmail() + ", recipient: " + senderRecipientHistory2.get().getRecipientEmail());
                    if (senderRecipientHistory2.get().getSenderUnreadCount()!=null){
                        senderRecipientHistory2.get().setSenderUnreadCount(senderRecipientHistory2.get().getSenderUnreadCount()+1);
                        senderRecipientHistoryRepository.save(senderRecipientHistory2.get());
                    }
                    else {
                     System.out.println("nor present!");
                     senderRecipientHistory2.get().setSenderUnreadCount(1);
                     senderRecipientHistoryRepository.save(senderRecipientHistory2.get());

                    }
            }

                userMessages.setSenderEmail(senderObj.toString());
                userMessages.setLastActionDate(String.valueOf(LocalDateTime.now()));
                userMessages.setEmailOfRecipient(recipient.get().getEmail());
                userMessages.setUnReadMessages(userMessages.getUnReadMessages()+1);

                if (recipient.get().getRole().equals(Role.JOB_SEEKER)){
                    if (employerRepository.findByEmail(senderObj.toString())!=null)
                        userMessages.setName(employerRepository.findByEmail(senderObj.toString()).getCompanyName());
                }
                message.setUserMessages(userMessages);
                message.setSender(senderObj.toString());
                message.setRead(false);

                message.setUserMessages(userMessages);

                messageRepository.save(message);
                userMessagesRepository.save(userMessages);

        }
        UserMessagesOneResponse userMessagesOneResponse2 = new UserMessagesOneResponse();
        userMessagesOneResponse2 = messageMapper.confirmMessage(message, userFromToken.getEmail().equals(message.getSender()));
        TypeResponse typeResponse2 = new TypeResponse();
        typeResponse2.setType("messageConfirm");
        typeResponse2.setResponseEntity(userMessagesOneResponse2);

        ObjectMapper objectMapper1 = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        senderSession.sendMessage(new TextMessage(objectMapper1.writeValueAsString(typeResponse2)));
        System.out.println("\n\nsent1!");
        message.setUserMessages(userMessages);
        message.setRead(true);


        userMessagesRepository.save(userMessages);
//        TypeResponse typeResponse1 = new TypeResponse();
//        typeResponse1.setType("newStatus");
//        NewStatus newStatus = new NewStatus();
//        if (recipientSession != null && recipientSession.isOpen()) {
//            newStatus.setStatus("read");
//        }
//        else {
//            newStatus.setStatus("unRead");
//        }
//        typeResponse1.setResponseEntity(newStatus);
//        senderSession.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(typeResponse1)));

        if (userMessageInfo.isEmpty()){
            System.out.println("the userMessageInfo is empty!");
            UserMessageInfo userMessageInfo1 = new UserMessageInfo();
            userMessageInfo1.setEmail(recipient.get().getEmail());
            userMessageInfo1.setUnReadMessages(userMessageInfo1.getUnReadMessages()+1);
            userMessages.setUserMessageInfo(userMessageInfo1);
            userMessageInfoRepository.save(userMessageInfo1);
        }
        else {
            userMessageInfo.get().setEmail(recipient.get().getEmail());
            userMessageInfo.get().setUnReadMessages(userMessageInfo.get().getUnReadMessages()+1);
            userMessages.setUserMessageInfo(userMessageInfo.get());
            userMessageInfoRepository.save(userMessageInfo.get());
        }
    }

    private void senderRecipientHistory(String sender, String recipient, Long messageId) {
        Optional<SenderRecipientHistory> senderRecipientHistory = senderRecipientHistoryRepository.findBySenderEmailAndRecipientEmail(sender,recipient);
        Optional<SenderRecipientHistory> senderRecipientHistory2 = senderRecipientHistoryRepository.findBySenderEmailAndRecipientEmail(recipient,sender);
        if (senderRecipientHistory.isEmpty() && senderRecipientHistory2.isEmpty()){
            System.out.println("\n\nits new list sender/recipient: ");
            SenderRecipientHistory senderRecipientHistory1 = new SenderRecipientHistory();
            senderRecipientHistory1.setSenderEmail(sender);
            senderRecipientHistory1.setRecipientEmail(recipient);
            List<Long> messageIds = new ArrayList<>();
            messageIds.add(messageId);
            senderRecipientHistory1.setMessageIds(messageIds);
            senderRecipientHistoryRepository.save(senderRecipientHistory1);
        }
        else {
            senderRecipientHistory = senderRecipientHistory2.isPresent() && senderRecipientHistory.isEmpty()? senderRecipientHistory2:senderRecipientHistory;
            List<Long> messageIds = senderRecipientHistory.get().getMessageIds();
            messageIds.add(messageId);
            senderRecipientHistory.get().setMessageIds(messageIds);
            senderRecipientHistoryRepository.save(senderRecipientHistory.get());
        }

    }



    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Добавьте логирование ошибок здесь, например:
        System.err.println("Error occurred with user: " + session.getAttributes().get("sender"));

        exception.printStackTrace();
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

        if ( (String) session.getAttributes().get("sender") != null){
            String userId = (String) session.getAttributes().get("sender");
            if (userId!=null)
                userSessions.remove(userId);
            if (userId != null) {
                removeSessionForUser(userId);
            }
            System.out.println("\n\n id of this user: " + userId);

            User user = userRepository.findByEmail(userId).orElseThrow();
            user.setOnline(false);
            userRepository.save(user);
        }
    }
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


    public WebSocketSession getSessionForUser(String email) {
        return userSessions.get(email);
    }

    public void addSessionForUser(String email, WebSocketSession session) {
        userSessions.put(email, session);
    }

    public void removeSessionForUser(String email) {
        userSessions.remove(email);
    }


}
