package com.example.hr_system.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "profession")
public class Profession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH,CascadeType.DETACH, CascadeType.PERSIST})
    private Position position;

    private String companyName;
    private int startedMonth;
    private int startedYear;
    private int endMonth;
    private int endYear;
    private boolean isWorkingNow;
    private String skills;

    @ManyToOne
    @JoinColumn(name = "job_seeker_id")
    private JobSeeker jobSeeker;
}
