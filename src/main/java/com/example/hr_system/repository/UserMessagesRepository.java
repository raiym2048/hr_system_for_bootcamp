package com.example.hr_system.repository;

import com.example.hr_system.entities.User;
import com.example.hr_system.entities.UserMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMessagesRepository extends JpaRepository<UserMessages, Long> {
        List<UserMessages> findBySenderEmail(String senderEmail);
        List<UserMessages> findBySenderEmailOrEmailOfRecipient(String senderEmail, String recipientEmail);
        List<UserMessages> findByEmailOfRecipient(String emailOfRecipient);
        List<UserMessages> findAllByEmailOfRecipientAndSenderEmail(String recipientEmail, String senderEmail);
        Optional<UserMessages> findByEmailOfRecipientAndSenderEmail(String one, String two);

}
