package com.example.hr_system.dto.responded;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespondedJobSeekersResponse {
    private String firstname;
    private String lastname;
    private String position;
    private String industry;
    private String experience;
    private String country_city;
    private String application_date;
    private String statusOfVacancy;
}
