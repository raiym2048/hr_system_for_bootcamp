package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.JobSeekerVacanciesResponses;
import com.example.hr_system.entities.Vacancy;
import com.example.hr_system.mapper.JobSeekerVacanciesResponsesMapper;
import com.example.hr_system.mapper.VacancyMapper;
import org.springframework.stereotype.Component;

@Component
public class JobSeekerVacanciesResponsesMapperImpl implements JobSeekerVacanciesResponsesMapper {
    private final VacancyMapper vacancyMapper;

    public JobSeekerVacanciesResponsesMapperImpl(VacancyMapper vacancyMapper) {
        this.vacancyMapper = vacancyMapper;
    }

    @Override
    public JobSeekerVacanciesResponses toDtos(Vacancy v) {
        JobSeekerVacanciesResponses jobSeekerVacanciesResponses = new JobSeekerVacanciesResponses();
        jobSeekerVacanciesResponses.setVacancyResponse(vacancyMapper.toDto(v));
        jobSeekerVacanciesResponses.setId(v.getId());
        jobSeekerVacanciesResponses.setOwnerName(v.getAbout_company());
        return jobSeekerVacanciesResponses;
    }
}
