package com.example.hr_system.dto.vacancy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class VacancyResponsesForAdmin {
    private Long vacancyId;
    private String companyName;
    private String position;
    private String applicationDate;
}
