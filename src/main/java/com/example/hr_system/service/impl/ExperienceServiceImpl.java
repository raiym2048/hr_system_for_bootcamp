package com.example.hr_system.service.impl;

import com.example.hr_system.dto.experience.ExperienceResponse;
import com.example.hr_system.mapper.ExperienceMapper;
import com.example.hr_system.repository.ExperienceRepository;
import com.example.hr_system.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {
    private final ExperienceMapper experienceMapper;
    private final ExperienceRepository experienceRepository;
    @Override
    public List<ExperienceResponse> findAll() {
        return experienceMapper.listExperienceResponseToDto(experienceRepository.findAll());

    }
}
