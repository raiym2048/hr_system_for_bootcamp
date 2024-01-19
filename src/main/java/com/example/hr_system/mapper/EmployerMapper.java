package com.example.hr_system.mapper;

import com.example.hr_system.dto.employer.EmployerResponse;
import com.example.hr_system.dto.employer.EmployerResponses;
import com.example.hr_system.entities.Employer;

import java.util.List;

public interface EmployerMapper {

    EmployerResponses toDto(Employer employer);

    List<EmployerResponses> toDtos(List<Employer>employerList);
}
