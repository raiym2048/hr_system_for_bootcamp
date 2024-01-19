package com.example.hr_system.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class UserMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long fileId;
    private String name;
    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    private List<Message> allMessages;
    private String lastActionDate;
    private String senderEmail;
    private String emailOfRecipient;
    private int unReadMessages;
    @ManyToOne()
    private UserMessageInfo userMessageInfo;
}
