package com.example.hr_system.dto.position;

import com.example.hr_system.dto.category.CategoryRequest;
import com.example.hr_system.dto.experience.ExperienceResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PositionRequest {


    private String name;

    private CategoryRequest categoryRequest;

    private ExperienceResponse experience;

}
