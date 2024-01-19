package com.example.hr_system.controller;

import com.example.hr_system.dto.experience.ExperienceResponse;
import com.example.hr_system.mapper.ExperienceMapper;
import com.example.hr_system.repository.ExperienceRepository;
import com.example.hr_system.service.ExperienceService;
import com.example.hr_system.service.impl.ExperienceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employer")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @GetMapping("/experience")
    public List<ExperienceResponse> experienceResponses() {
        return experienceService.findAll();
    }
}
