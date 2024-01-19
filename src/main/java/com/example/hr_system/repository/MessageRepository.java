package com.example.hr_system.repository;

import com.example.hr_system.entities.Message;
import com.sun.mail.imap.protocol.ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndRecipient(String sender, String recipient);
    abstract java.util.List<Message> findByIdInOrderByTimeDesc(Iterable<Long> ids);
    Integer findByRecipientAndIsRead(String recipient, Boolean isRead);
    List<Message> findBySenderOrRecipient(String sender, String recipient);
    List<Message> findBySenderOrRecipientOrderByTimeDesc(String sender, String recipient);
    List<Message> findByRecipientAndIsReadOrderByTimeDesc(String recipient, Boolean isRead);
    List<Message> findAllByOrderByTimeDesc();

    List<Message> findAllBySender(String sender);
    List<Message> findAllByRecipient(String sender);

    List<Message> findByIdInOrderByTimeDesc(List<Long> ids);
   // List<Message> findByRecipientAndIsRead()
}