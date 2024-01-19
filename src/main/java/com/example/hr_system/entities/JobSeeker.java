package com.example.hr_system.entities;


import com.example.hr_system.enums.Education;
import com.example.hr_system.enums.Month;
import com.example.hr_system.enums.Role;
import com.example.hr_system.enums.StatusOfJobSeeker;
import javax.persistence.*;
import lombok.*;
import org.hibernate.engine.internal.Cascade;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_seeker_table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobSeeker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "jobSeeker")
    private User user;


    private LocalDate birthday;
    private String country;
    private String city;
    private String address;
    @Enumerated(EnumType.STRING)
    private StatusOfJobSeeker statusOfJobSeeker;
    private LocalDateTime userApplicationDate;


    private String phoneNumber;

    @Column(name = "about")
    private String about;


    private String firstname;
    private String lastname;

    private String email;
    private String password;



    @OneToOne(cascade = CascadeType.ALL)
    private FileData resume;

    @OneToOne(cascade = CascadeType.ALL)
    private FileData image;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private Role role;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "jobSeekers")
    private List<Vacancy> vacancies;

    @ManyToMany(mappedBy = "favorites")
    private List<Employer> employers;

    private Long isFavorite;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "jobSeeker")
    private List<Profession> professions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobSeeker")
    private List<EducationJob> educationJobs;

    @ManyToOne
    @JoinColumn(name = "experience_id")
    private Experience experienceJ;




}
