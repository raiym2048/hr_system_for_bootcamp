package com.example.hr_system.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class SenderRecipientHistory {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String senderEmail;
    private String recipientEmail;

    @ElementCollection
    private List<Long> messageIds;

    private Integer senderUnreadCount;
    private Integer recipientUnreadCount;
}
