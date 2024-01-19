package com.example.hr_system.dto.vacancy;

import com.example.hr_system.dto.contactInformation.ContactInformationRequest;
import com.example.hr_system.dto.contactInformation.ContactInformationResponse;
import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.dto.position.PositionResponse;
import com.example.hr_system.dto.salary.SalaryRequest;
import com.example.hr_system.dto.salary.SalaryResponse;
import com.example.hr_system.entities.ContactInformation;
import com.example.hr_system.enums.TypeOfEmployment;
import com.example.hr_system.enums.Valute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VacancyResponse {
    private String creationDate;
    private String position;
    private Long id;

    private FileResponse image;
    private String companyName;
    private String about_company;
    private String description;
    private String skills;
    private ContactInformationResponse contactInformationResponse;
    private String additionalInformation;

    private int respondedCount;
    private String country;
    private String city;
    private String category;
    private TypeOfEmployment typeOfEmploymentS;
    private String  requiredExperience;
    private SalaryResponse salaryResponse;
    private Long searchCounter;

    private String industry;
    private String StatusOfVacancy;
   // private String status
}
