package com.example.hr_system.dto.profession;

import com.example.hr_system.entities.Position;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
public class ProfessionResponse {
    private Long id;
    private String position;

    private String companyName;
    private String startedMonth;
    private String startedYear;
    private String endMonth;
    private String endYear;
    private boolean isWorkingNow;
    private String skills;
}
