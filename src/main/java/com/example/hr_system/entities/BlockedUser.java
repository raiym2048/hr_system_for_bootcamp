package com.example.hr_system.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blocked_table")
public class BlockedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean viewingCompanyData;
    private Boolean viewingAndSearchingForVacancies;
    private Boolean viewTheStatusOfResponded;
    private Boolean communicationWithEmployers;
    private Boolean viewingCandidateData;
    private Boolean vacancyAndHiringManagement;
    private Boolean communicationWithJobSeekers;

    @OneToOne(mappedBy = "blockedUser")
    private User user;
}