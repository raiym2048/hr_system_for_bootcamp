package com.example.hr_system.controller;


import com.example.hr_system.dto.position.CandidateResponse;
import com.example.hr_system.mapper.PositionMapper;
import com.example.hr_system.repository.PositionRepository;
import com.example.hr_system.service.PositionService;
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
public class PositionController {
    private final PositionService positionService;

    @GetMapping("/positions")
    public List<CandidateResponse> positions() {
        return positionService.getAllPositions();
    }


}
