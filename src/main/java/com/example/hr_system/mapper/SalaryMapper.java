package com.example.hr_system.mapper;

import com.example.hr_system.dto.salary.SalaryRequest;
import com.example.hr_system.dto.salary.SalaryResponse;
import com.example.hr_system.entities.Salary;

import java.util.List;

public interface SalaryMapper {

    SalaryResponse toDto(Salary salary);

    List<SalaryResponse> toDtos(List<Salary> salaries);

    Salary toEntity(SalaryRequest salaryRequest);
}
