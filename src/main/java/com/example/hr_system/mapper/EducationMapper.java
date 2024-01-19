package com.example.hr_system.mapper;

import com.example.hr_system.dto.education.EducationRequest;
import com.example.hr_system.dto.education.EducationResponse;
import com.example.hr_system.entities.EducationJob;
import com.example.hr_system.entities.JobSeeker;

import java.util.List;

public interface EducationMapper {
    List<EducationResponse> toDtos(List<EducationJob> educationJobs, JobSeeker jobSeeker);

    EducationResponse todto(EducationJob educationJob, JobSeeker jobSeeker);

    List<EducationJob> RequestToDtoS(List<EducationRequest> educationRequests, JobSeeker jobSeeker);

    EducationJob RequestToDto(EducationRequest educationJob, JobSeeker jobSeeker);
}
