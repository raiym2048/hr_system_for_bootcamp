package com.example.hr_system.service;

import com.example.hr_system.dto.experience.ExperienceResponse;

import java.util.List;

public interface ExperienceService {
    List<ExperienceResponse> findAll();
}
