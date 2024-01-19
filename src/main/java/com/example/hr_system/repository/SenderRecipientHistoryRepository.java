package com.example.hr_system.repository;

import com.example.hr_system.entities.SenderRecipientHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SenderRecipientHistoryRepository extends JpaRepository<SenderRecipientHistory, Long> {
    Optional<SenderRecipientHistory> findByRecipientEmail(String email);
    List<SenderRecipientHistory> findAllByRecipientEmail(String email);
    List<SenderRecipientHistory> findAllByRecipientEmailOrSenderEmail(String email, String email2);
    Optional<SenderRecipientHistory> findBySenderEmailAndRecipientEmail(String sender, String recipient);
    Optional<SenderRecipientHistory> findByRecipientEmailAndSenderEmail(String recipient, String sender);
    Optional<SenderRecipientHistory> findBySenderEmailOrRecipientEmail(String sender, String recipient);
    List<SenderRecipientHistory> findAllBySenderEmailOrRecipientEmail(String sender, String recipient);
    Optional<SenderRecipientHistory> findBySenderEmail(String email);
}
