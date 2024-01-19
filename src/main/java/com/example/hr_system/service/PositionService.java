package com.example.hr_system.service;

import com.example.hr_system.dto.position.CandidateResponse;
import com.example.hr_system.dto.position.PositionRequest;
import com.example.hr_system.entities.Position;

import java.util.List;

public interface PositionService {
    Position convertToEntity(PositionRequest positionRequest);

    List<CandidateResponse> getAllPositions();
}
