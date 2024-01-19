package com.example.hr_system.dto.vacancy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VacancyResponseForPopularCategories {
    private String categoryName;
    private Integer counterVacancy;
}
