package com.example.hr_system.dto.jobSeeker;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidateResponses {
    Long candidateId;
    boolean red = false;
    Long imageId;
    String firstname;
    String lastname;
    String position;
    String experience;
    String country;
    String city;

}
