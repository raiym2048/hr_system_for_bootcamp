package com.example.hr_system.mapper;

import com.example.hr_system.dto.vacancy.RespondedVacancyResponse;
import com.example.hr_system.entities.JobSeekerVacancyInformation;

import java.util.List;

public interface JobSeekerVacancyInformationMapper {
    List<RespondedVacancyResponse> toRespondedVacancyResponseFromInformS(List<JobSeekerVacancyInformation> jobSeekerVacancyInformations);

    RespondedVacancyResponse toDto(JobSeekerVacancyInformation jobSeekerVacancyInformation);
}
