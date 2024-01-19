package com.example.hr_system.mapper.impl;


import com.example.hr_system.dto.salary.SalaryRequest;
import com.example.hr_system.dto.salary.SalaryResponse;
import com.example.hr_system.entities.Salary;
import com.example.hr_system.mapper.SalaryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@AllArgsConstructor

public class SalaryMapperImpl implements SalaryMapper {


    @Override
    public SalaryResponse toDto(Salary salary) {
        if (salary == null) {
            return null;
        }


        SalaryResponse salaryResponse = new SalaryResponse();
        salaryResponse.setId(salary.getId());
        salaryResponse.setValute(salary.getValute());
        salaryResponse.setSalaryType(salary.getSalaryType());
        salaryResponse.setSalarySum(salary.getSalarySum());
        return salaryResponse;
    }

    @Override
    public List<SalaryResponse> toDtos(List<Salary> salaries) {
        List<SalaryResponse> salaryResponses = new ArrayList<>();
        for (Salary salary : salaries) {
            salaryResponses.add(toDto(salary));
        }
        return salaryResponses;
    }

    @Override
    public Salary toEntity(SalaryRequest salaryRequest) {
        Salary salary = new Salary();
        if (salaryRequest==null)
            throw new NotFoundException("salary is null");

        salary.setSalaryType(salaryRequest.getSalaryType()==null?null:
                salaryRequest.getSalaryType());
        salary.setSalarySum(salaryRequest.getSalarySum());
        salary.setValute(salaryRequest.getValute()==null?null:
                salaryRequest.getValute());

        //salary.setSalaryType(salaryRequest.getTypeOfEmployment());
        return salary;
    }
}
