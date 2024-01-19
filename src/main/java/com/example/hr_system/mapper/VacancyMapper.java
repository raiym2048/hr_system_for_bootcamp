package com.example.hr_system.mapper;

import com.example.hr_system.dto.salary.SalaryRequest;
import com.example.hr_system.dto.vacancy.*;
import com.example.hr_system.entities.JobSeeker;
import com.example.hr_system.entities.Salary;
import com.example.hr_system.entities.Vacancy;

import java.util.List;
import java.util.Optional;

public interface VacancyMapper {

    VacancyResponse toDto(Vacancy vacancy);

    List<VacancyResponse> toDtos(List<Vacancy> vacancies);

    VacancyResponse requestToResponse(VacancyRequest vacancyRequest);

    Salary toEntity(SalaryRequest salaryRequest);

    RespondedVacancyResponse toRespondedVacancyResponse(JobSeeker jobSeeker,Vacancy vacancy);

    List<RespondedVacancyResponse> toListRespondedVacancyResponse(JobSeeker jobSeeker, List<Vacancy> vacancies);

    AboutVacancy toAboutVacancyResponse(Vacancy vacancy);

    VacancyResponsesForAdmin toDtoVacancyForAdmin(Vacancy vacancy);

    List<VacancyResponsesForAdmin> toDtosVacanciesForAdmin(List<Vacancy> vacancies);

}
