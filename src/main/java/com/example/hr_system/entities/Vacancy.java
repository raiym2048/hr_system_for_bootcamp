package com.example.hr_system.entities;


import com.example.hr_system.enums.StatusOfVacancy;
import com.example.hr_system.enums.TypeOfEmployment;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "vacancies")
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(3000)")
    private String about_company;
    private String industry;
    @Column(columnDefinition = "varchar(3000)")
    private String description;
    private String skills;
    @Enumerated(EnumType.STRING)
    private TypeOfEmployment typeOfEmploymentS;
    private String experience;
    private String additionalInformation;
    private LocalDateTime creationDate;
    @Enumerated(EnumType.STRING)
    private StatusOfVacancy statusOfVacancy;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<JobSeeker> jobSeekers;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE})
    private Employer employer;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Position position;
    @OneToOne(cascade = CascadeType.ALL)
    private Salary salary;
    private Integer response;
    private Long searchCounter;
    private Boolean isResponse = false;

    @OneToOne(cascade = CascadeType.ALL)
    private ContactInformation contactInformation;

    @OneToOne(cascade = CascadeType.ALL)
    private FileData resume;
    private String days;


}
