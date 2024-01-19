package com.example.hr_system.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "list_support_table")
@Entity
public class ListSupport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String personName;
    private String personEmail;
    private Integer personPhoneNumber;

    @Column(columnDefinition = "TEXT")
    private String message;
    private String dateSent;
}
