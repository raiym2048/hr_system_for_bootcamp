package com.example.hr_system.entities;

import com.example.hr_system.enums.Education;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "education")
public class EducationJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Education education;
    private String university;
    private int startedMonth;
    private int startedYear;
    private int endMonth;
    private int endYear;
    private boolean isStudying;

    @ManyToOne
    @JoinColumn(name = "job_seeker_id")
    private JobSeeker jobSeeker;
}
