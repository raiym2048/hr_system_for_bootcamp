package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.profession.ProfessionRequest;
import com.example.hr_system.dto.profession.ProfessionResponse;
import com.example.hr_system.entities.JobSeeker;
import com.example.hr_system.entities.Profession;
import com.example.hr_system.mapper.ProfessionMapper;
import com.example.hr_system.repository.PositionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ProfessionMapperImpl implements ProfessionMapper {
    private final PositionRepository positionRepository;
    @Override
    public List<ProfessionResponse> toDtos(List<Profession> professions, JobSeeker jobSeeker) {
        List<ProfessionResponse> professionResponses = new ArrayList<>();
        for(Profession profession: professions){
            professionResponses.add(toDto(profession, jobSeeker));
        }
        return professionResponses;
    }



    @Override
    public ProfessionResponse toDto(Profession profession, JobSeeker jobSeeker) {
        ProfessionResponse professionResponse = new ProfessionResponse();
        if (jobSeeker.getProfessions().size()>0){
            if (jobSeeker.getProfessions().get(0).getPosition()!=null)
                professionResponse.setPosition(jobSeeker.getProfessions().get(jobSeeker.getProfessions().size()-1).getPosition().getName());
        }
        professionResponse.setCompanyName(profession.getCompanyName());
        professionResponse.setSkills(profession.getSkills());
        professionResponse.setStartedMonth(String.valueOf(profession.getStartedMonth()));
        professionResponse.setStartedYear(String.valueOf(profession.getStartedYear()));
        professionResponse.setEndMonth(String.valueOf(profession.getEndMonth()));
        professionResponse.setEndYear(String.valueOf(profession.getEndYear()));
        return professionResponse;
    }

    @Override
    public List<Profession> requestToEntitys(List<ProfessionRequest> professionRequests) {
        List<Profession> professions = new ArrayList<>();
        for(ProfessionRequest profession: professionRequests){
            professions.add(requestToEntity(profession));
        }
        return professions;
    }

    private Profession requestToEntity(ProfessionRequest profession) {
        Profession profession1 = new Profession();
        profession1.setPosition(positionRepository.findByName(profession.getPosition()));
        profession1.setCompanyName(profession.getCompanyName());
        profession1.setStartedMonth(profession.getStartedMonth());
        profession1.setStartedYear(profession.getStartedYear());
        profession1.setEndMonth(profession.getEndMonth());
        profession1.setEndYear(profession.getEndYear());
        profession1.setSkills(profession.getSkills());
        return profession1;
    }

    @Override
    public List<Profession> RequestToDtoS(List<ProfessionRequest> professionRequests, JobSeeker jobSeeker) {
        List<Profession> professions = new ArrayList<>();
        for (ProfessionRequest professionRequest: professionRequests)
            professions.add(RequestToDto(professionRequest, jobSeeker));
        return professions;
    }

    private Profession RequestToDto(ProfessionRequest profession, JobSeeker jobSeeker) {
        Profession professionResponse = new Profession();
        professionResponse.setPosition(positionRepository.findByName(profession.getPosition()));
        professionResponse.setCompanyName(profession.getCompanyName());
        professionResponse.setSkills(profession.getSkills());
        professionResponse.setStartedMonth(profession.getStartedMonth());
        professionResponse.setStartedYear(profession.getStartedYear());
        if (profession.isWorkingNow()){
            professionResponse.setEndMonth(LocalDateTime.now().getMonthValue());
            professionResponse.setEndYear(LocalDateTime.now().getYear());
        }
        else {
            professionResponse.setEndMonth(profession.getEndMonth());
            professionResponse.setEndYear(profession.getEndYear());
        }
        professionResponse.setJobSeeker(jobSeeker);

        return professionResponse;
    }
}
