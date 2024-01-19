package com.example.hr_system.dto.vacancy;

import com.example.hr_system.dto.contactInformation.ContactInformationRequest;
import com.example.hr_system.dto.contactInformation.ContactInformationResponse;
import com.example.hr_system.dto.position.PositionRequest;
import com.example.hr_system.dto.salary.SalaryRequest;
import com.example.hr_system.entities.ContactInformation;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class VacancyRequest {
    private String about_company;
    private String position;
    private String industry;
    private String description;
    private String skills;
    private SalaryRequest salaryResponse;
    private String typeOfEmploymentS;
    private String experience;
    private ContactInformationRequest contactInformationResponse;
    private String additionalInformation;
}
