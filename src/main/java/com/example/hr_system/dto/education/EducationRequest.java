package com.example.hr_system.dto.education;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EducationRequest {
    private String education;
    private String university;
    private int endMonth;
    private int endYear;
    private boolean isStudying;
}
