package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.vacancy.RespondedVacancyResponse;
import com.example.hr_system.entities.Employer;
import com.example.hr_system.entities.JobSeekerVacancyInformation;
import com.example.hr_system.entities.Vacancy;
import com.example.hr_system.mapper.FileMapper;
import com.example.hr_system.mapper.JobSeekerVacancyInformationMapper;
import com.example.hr_system.repository.EmployerRepository;
import com.example.hr_system.repository.UserRepository;
import com.example.hr_system.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JobSeekerVacancyInformationMapperImpl implements JobSeekerVacancyInformationMapper {
    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;
    private final VacancyRepository vacancyRepository;
    private final FileMapper fileMapper;
    @Override
    public List<RespondedVacancyResponse> toRespondedVacancyResponseFromInformS(List<JobSeekerVacancyInformation> jobSeekerVacancyInformations) {
        List<RespondedVacancyResponse> vacancyResponses = new ArrayList<>();
        for (JobSeekerVacancyInformation jobSeekerVacancyInformation : jobSeekerVacancyInformations) {
            vacancyResponses.add(toDto(jobSeekerVacancyInformation));
        }

        return vacancyResponses;
    }
    @Override
    public RespondedVacancyResponse toDto(JobSeekerVacancyInformation jobSeekerVacancyInformation) {
        Employer employer =  vacancyRepository.findById(jobSeekerVacancyInformation.getVacancyId()).get().getEmployer();
        if (employer==null)
            throw new NotFoundException("employer not found!");
        Vacancy vacancy = vacancyRepository.findById(jobSeekerVacancyInformation.getVacancyId()).get();
        RespondedVacancyResponse vacancyResponse = new RespondedVacancyResponse();
        vacancyResponse.setVacancyId(jobSeekerVacancyInformation.getVacancyId());
        vacancyResponse.setImage(employer.getResume()!=null?fileMapper.toDto(employer.getResume()):null);
        vacancyResponse.setCompanyName(employer.getCompanyName());
        vacancyResponse.setPosition(vacancy.getPosition()!=null?vacancy.getPosition().getName():null);
        vacancyResponse.setCategory(vacancy.getPosition().getCategory().getName());
        vacancyResponse.setApplicationDate(String.valueOf(jobSeekerVacancyInformation.getLocalDateTime()));
        vacancyResponse.setStatusOfJobSeeker(jobSeekerVacancyInformation.getStatusOfJobSeeker());
        return vacancyResponse;
    }

//    private Long vacancyId;
//    private FileResponse image;
//    private String companyName;
//    private String position;
//    private String category;
//    private String applicationDate;
//    private StatusOfJobSeeker statusOfJobSeeker;

}
