package com.example.hr_system.entities;

import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private Long imageId;

    private String arrivedDate;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "is_send")
    private Boolean isSend;
}
