package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.experience.ExperienceResponse;
import com.example.hr_system.entities.Experience;
import com.example.hr_system.mapper.ExperienceMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExperienceMapperImpl implements ExperienceMapper {

    @Override
    public List<ExperienceResponse> listExperienceResponseToDto(List<Experience> experiences){
        List<ExperienceResponse> experienceResponses = new ArrayList<>();
        for (Experience experience: experiences){
            experienceResponses.add(ExperienceResponseToDto(experience));
        }
        return experienceResponses;
    }


    @Override
    public ExperienceResponse ExperienceResponseToDto(Experience experience) {
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setName(experience.getName());
        return experienceResponse;
    }
}
