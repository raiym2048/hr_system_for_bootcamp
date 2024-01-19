package com.example.hr_system.mapper;

import com.example.hr_system.dto.JobSeekerVacanciesResponses;
import com.example.hr_system.entities.Vacancy;

public interface JobSeekerVacanciesResponsesMapper {
    JobSeekerVacanciesResponses toDtos(Vacancy v);
}
