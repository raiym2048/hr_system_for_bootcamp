package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.education.EducationRequest;
import com.example.hr_system.dto.education.EducationResponse;
import com.example.hr_system.entities.EducationJob;
import com.example.hr_system.entities.JobSeeker;
import com.example.hr_system.enums.Education;
import com.example.hr_system.mapper.EducationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class EducationMapperImpl implements EducationMapper {

    @Override
    public List<EducationResponse> toDtos(List<EducationJob> educationJobs, JobSeeker jobSeeker) {
        List<EducationResponse> educationResponses = new ArrayList<>();

        for(EducationJob educationJob: educationJobs){
            educationResponses.add(todto(educationJob, jobSeeker));
        }
        return educationResponses;
    }

    @Override
    public EducationResponse todto(EducationJob educationJob, JobSeeker jobSeeker) {
        EducationResponse educationResponse = new EducationResponse();
        educationResponse.setUniversity(educationJob.getUniversity());
        educationResponse.setEducation(educationJob.getEducation().name());
        educationResponse.setEndMonth(String.valueOf(educationJob.getEndMonth()));
        educationResponse.setEndYear(String.valueOf(educationJob.getEndYear()));

        return educationResponse;
    }

    @Override
    public List<EducationJob> RequestToDtoS(List<EducationRequest> educationRequests, JobSeeker jobSeeker) {
        List<EducationJob> educationResponses = new ArrayList<>();
        for(EducationRequest educationJob: educationRequests){
            educationResponses.add(RequestToDto(educationJob, jobSeeker));
        }
        return educationResponses;
    }

    @Override
    public EducationJob RequestToDto(EducationRequest educationJob, JobSeeker jobSeeker) {

        EducationJob educationResponse = new EducationJob();
        educationResponse.setUniversity(educationJob.getUniversity());
        educationResponse.setEducation(Education.valueOf(educationJob.getEducation()));
        if (educationJob.isStudying()){
            educationResponse.setEndMonth(LocalDateTime.now().getMonthValue());
            educationResponse.setEndYear(LocalDateTime.now().getYear());
        }
        educationResponse.setEndMonth(educationJob.getEndMonth());
        educationResponse.setEndYear(educationJob.getEndYear());
        educationResponse.setJobSeeker(jobSeeker);

        return educationResponse;
    }




}
