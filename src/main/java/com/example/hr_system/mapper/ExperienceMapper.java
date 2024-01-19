package com.example.hr_system.mapper;

import com.example.hr_system.dto.experience.ExperienceResponse;
import com.example.hr_system.entities.Experience;

import java.util.List;

public interface ExperienceMapper {
    List<ExperienceResponse> listExperienceResponseToDto(List<Experience> experiences);

    ExperienceResponse ExperienceResponseToDto(Experience experience);
}
