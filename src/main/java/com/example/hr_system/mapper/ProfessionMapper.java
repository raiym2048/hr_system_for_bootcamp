package com.example.hr_system.mapper;

import com.example.hr_system.dto.profession.ProfessionRequest;
import com.example.hr_system.dto.profession.ProfessionResponse;
import com.example.hr_system.entities.JobSeeker;
import com.example.hr_system.entities.Profession;

import java.util.List;

public interface ProfessionMapper {
    List<ProfessionResponse> toDtos(List<Profession> professions, JobSeeker jobSeeker);

    List<Profession> RequestToDtoS(List<ProfessionRequest> professionRequests, JobSeeker jobSeeker);

    ProfessionResponse toDto(Profession profession, JobSeeker jobSeeker);

    List<Profession> requestToEntitys(List<ProfessionRequest> professionRequests);
}
