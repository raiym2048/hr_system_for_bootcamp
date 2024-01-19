package com.example.hr_system.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    private String recipient;
    private String content;
    private LocalDateTime time;
    private Long scribeId;

    @JsonProperty("isRead")
    private boolean isRead = false;

    private int unreadCountMessages;
    @ElementCollection
    private List<Long> fileIds;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserMessages userMessages;
    private Boolean amIAuthor;


//    private boolean isOnline;

    // Getters, setters, constructors, etc.
}



