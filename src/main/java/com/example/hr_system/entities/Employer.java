package com.example.hr_system.entities;


import javax.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String companyName;

    private String email;

    private String password;

    private String address;

    private String phoneNumber;

    private String city;

    private String aboutCompany;

    private String country;





    @OneToOne(cascade = CascadeType.ALL, mappedBy = "employer")
    private User user;



    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employer")
    private List<Vacancy> vacancyList;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<JobSeeker> favorites;

    @OneToOne(cascade = CascadeType.ALL)
    private FileData resume;


//sekflm





}

