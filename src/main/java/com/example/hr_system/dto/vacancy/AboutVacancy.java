package com.example.hr_system.dto.vacancy;


import com.example.hr_system.dto.contactInformation.ContactInformationResponse;
import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.dto.salary.SalaryResponse;
import com.example.hr_system.enums.TypeOfEmployment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class AboutVacancy {
    private String creationDate;
    private String position;
    private Long id;

    private FileResponse image;
    private String companyName;
    private String about_company;
    private String description;
    private String skills;
    private ContactInformationResponse contactInformationResponse;
    private String street;
    private String phoneNumber;
    private String additionalInformation;

    private int respondedCount;
    private String category;
    private TypeOfEmployment typeOfEmploymentS;
    private String  requiredExperience;
    private SalaryResponse salaryResponse;


    private String industry;
    private String StatusOfVacancy;

}
