package com.example.hr_system.dto;

import com.example.hr_system.dto.contactInformation.ContactInformationResponse;
import com.example.hr_system.dto.jobSeeker.JobSeekerResponses;
import com.example.hr_system.dto.position.PositionResponse;
import com.example.hr_system.dto.salary.SalaryResponse;
import com.example.hr_system.entities.JobSeeker;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeVacanciesResponses {
    private Long id;
    private PositionResponse positionResponse;
    private SalaryResponse salaryResponse;
    private ContactInformationResponse contactInformationResponse;
    private List<JobSeekerResponses> jobSeekers;
    private Date date;
    private String status;
    private Integer response;

}
