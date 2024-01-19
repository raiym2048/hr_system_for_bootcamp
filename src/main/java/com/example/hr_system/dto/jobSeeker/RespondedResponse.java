package com.example.hr_system.dto.jobSeeker;

import com.example.hr_system.enums.StatusOfJobSeeker;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespondedResponse {
    private Long vacancyId;
    private Long id;
    private Integer respondedCount;
    private String firstname;
    private String lastname;
    private String position;
    private String category;
    private String experience;
    private String country;
    private String city;
    private String localDate;
    private StatusOfJobSeeker statusOfJobSeeker;
}