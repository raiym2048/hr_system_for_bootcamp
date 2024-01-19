package com.example.hr_system.controller;

import com.example.hr_system.dto.message.MessageResponse;
import com.example.hr_system.dto.message.TypeResponse;
import com.example.hr_system.dto.userMessages.UserMessagesResponse;
import com.example.hr_system.entities.Message;
import com.example.hr_system.entities.User;
import com.example.hr_system.entities.UserMessageInfo;
import com.example.hr_system.entities.UserMessages;
import com.example.hr_system.repository.MessageRepository;
import com.example.hr_system.service.CategoryService;
import com.example.hr_system.service.ChatService;
import com.example.hr_system.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/chat")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class ChatController {

    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final NotificationService notificationService;
    private final CategoryService categoryService;

    @GetMapping("/openUser/{userId}")
    public UserMessagesResponse openUser(@RequestHeader(name = "Authorization") String token,
                                         @PathVariable Long userId){
        return chatService.openUserById(userId);
    }



    @PostMapping("/connectToUser")
    public void getAllUsersMessages(@RequestHeader(name = "Authorization") String token,
                                             @RequestParam(required = false) String email){

         categoryService.getAllUsersMessages(token, email);
    }

    @GetMapping("/allMyUnReadMessagesCount")
    public Integer geAllUsersUnReadMessages(@RequestHeader(name = "Authorization") String token){
        return chatService.getAllUsersUnReadMessagesCount(token);
    }
//    @GetMapping("/allMyUnReadMessages")
//    public List<MessageResponse> getAllUnreadMessages(@RequestHeader(name = "Authorization") String token){
//        return chatService.getAllUnreadMessages(token);
//    }

    @GetMapping("/getAllHistoryChat")
    public List<UserMessagesResponse> getAllHistoryChat(@RequestHeader(name = "Authorization") String token){
       return chatService.getAllHistoryChat(token);
    }

    @MessageMapping("/fetchUnreadNotifications")
    @SendTo("/topic/unreadNotifications")
    public Integer getIsNotReadNotifications(@PathVariable Long userId){
        return notificationService.countUnreadNotificationsForUser(userId);
    }

}

//eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvbmVAaWFhdS5lZHUua2ciLCJhdXRoIjp7ImF1dGhvcml0eSI6IkpPQl9TRUVLRVIifSwiaWF0IjoxNjk2Mzg5ODA0LCJleHAiOjE2OTY2ODk4MDR9.ix1WquMeJvdjXrYBGsTxmbyK4nkRomM2XayEP95MzzU
//eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0d29AaWFhdS5lZHUua2ciLCJhdXRoIjp7ImF1dGhvcml0eSI6IkpPQl9TRUVLRVIifSwiaWF0IjoxNjk2Mzg5ODE5LCJleHAiOjE2OTY2ODk4MTl9.1k-qQEwRp8TjzfK2F9l95Bl7U9NKX6UjWvix-gppo6g
//eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aHJlZUBpYWF1LmVkdS5rZyIsImF1dGgiOnsiYXV0aG9yaXR5IjoiSk9CX1NFRUtFUiJ9LCJpYXQiOjE2OTYzODk4MzMsImV4cCI6MTY5NjY4OTgzM30.uFC_ksUWy6jfW5MqIn5AJp-Egi0dvuTpczSiLvekpJI