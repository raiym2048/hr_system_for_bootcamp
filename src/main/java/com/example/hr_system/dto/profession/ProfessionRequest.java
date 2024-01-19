package com.example.hr_system.dto.profession;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessionRequest {
    private String position;

    private String companyName;
    private int startedMonth;
    private int startedYear;
    private int endMonth;
    private int endYear;
    private boolean isWorkingNow;
    private String skills;
}
