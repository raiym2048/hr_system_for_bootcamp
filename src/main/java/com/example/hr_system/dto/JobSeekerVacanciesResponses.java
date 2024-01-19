package com.example.hr_system.dto;

import com.example.hr_system.dto.contactInformation.ContactInformationResponse;
import com.example.hr_system.dto.position.PositionResponse;
import com.example.hr_system.dto.salary.SalaryResponse;
import com.example.hr_system.dto.vacancy.VacancyResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class JobSeekerVacanciesResponses {
    private Long id;
    private String ownerName;
    private VacancyResponse vacancyResponse;

}
