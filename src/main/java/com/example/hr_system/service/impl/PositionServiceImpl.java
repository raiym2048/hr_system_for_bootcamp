package com.example.hr_system.service.impl;

import com.example.hr_system.dto.category.CategoryRequest;
import com.example.hr_system.dto.position.CandidateResponse;
import com.example.hr_system.dto.position.PositionRequest;
import com.example.hr_system.entities.Category;
import com.example.hr_system.entities.Position;
import com.example.hr_system.mapper.PositionMapper;
import com.example.hr_system.repository.PositionRepository;
import com.example.hr_system.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionMapper positionMapper;
    private final PositionRepository positionRepository;
    @Override
    public Position convertToEntity(PositionRequest positionRequest) {
        if (positionRequest == null) {
            return null;
        }
        Position position = new Position();
        position.setName(positionRequest.getName());
        CategoryRequest categoryRequest = positionRequest.getCategoryRequest();
        Category category = new Category();
        category.setName(categoryRequest.getName());
        position.setCategory(category);
        return position;
    }

    @Override
    public List<CandidateResponse> getAllPositions() {
        return positionMapper.listCandidatePositionToDto(positionRepository.findAll());
    }

}
