package com.example.hr_system.service.impl;

import com.example.hr_system.config.MyHandler;
import com.example.hr_system.dto.category.CategoryRequest;
import com.example.hr_system.dto.category.CategoryResponse;
import com.example.hr_system.entities.*;
import com.example.hr_system.mapper.CategoryMapper;
import com.example.hr_system.repository.CategoryRepository;
import com.example.hr_system.repository.MessageRepository;
import com.example.hr_system.repository.SenderRecipientHistoryRepository;
import com.example.hr_system.repository.UserRepository;
import com.example.hr_system.service.CategoryService;
import com.example.hr_system.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserRepository userRepository;
    private final ChatService chatService;
    private final MessageRepository messageRepository;
    private final SenderRecipientHistoryRepository senderRecipientHistoryRepository;
    private final MyHandler myHandler;



    @Override
    public List<CategoryResponse> getAllCategory() {
        return categoryMapper.toDtos(categoryRepository.findAll());
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return categoryMapper.toDto(findById(id));
    }
    private Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());

        categoryRepository.save(category);
        return new CategoryResponse(category.getId(),category.getName());
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category1 = categoryRepository.findById(id).get();

        category1.setName(categoryRequest.getName());
        return categoryMapper.toDto(categoryRepository.save(category1));
    }
    @Override
    public void getAllUsersMessages(String token, String email) {
        User sender = chatService.getUsernameFromToken(token);
        myHandler.connectConfirm(sender, userRepository.findByEmail(email).get());


        List<Message> messages = messageRepository.findAllByRecipient(sender.getEmail());
        for(Message message: messages){
            message.setRead(true);
            messageRepository.save(message);
        }
        Optional<SenderRecipientHistory> senderRecipientHistory = senderRecipientHistoryRepository.findBySenderEmailAndRecipientEmail(sender.getEmail(), email);
        Optional<SenderRecipientHistory> senderRecipientHistory2 = senderRecipientHistoryRepository.findBySenderEmailAndRecipientEmail(email, sender.getEmail());
        if (senderRecipientHistory.isPresent()){
            if (senderRecipientHistory.get().getSenderEmail().equals(sender.getEmail())){
                senderRecipientHistory.get().setSenderUnreadCount(0);
            }
            else if(senderRecipientHistory.get().getRecipientEmail().equals(sender.getEmail())){
                senderRecipientHistory.get().setRecipientUnreadCount(0);
            }
            senderRecipientHistoryRepository.save(senderRecipientHistory.get());
        }
        else if (senderRecipientHistory2.isPresent()){
            if (senderRecipientHistory2.get().getSenderEmail().equals(sender.getEmail())){
                senderRecipientHistory2.get().setSenderUnreadCount(0);
            }
            else if(senderRecipientHistory2.get().getRecipientEmail().equals(sender.getEmail())){
                senderRecipientHistory2.get().setRecipientUnreadCount(0);
            }
            senderRecipientHistoryRepository.save(senderRecipientHistory2.get());

        }
    }

}

