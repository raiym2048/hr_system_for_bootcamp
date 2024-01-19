package com.example.hr_system.service.impl;


import com.example.hr_system.entities.Profession;
import com.example.hr_system.service.ProfessionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProfessionServiceImpl implements ProfessionService {

    @Override
    public String getExperience(List<Profession> professions) {
            int totalMonths = 0;
            int currentYear = LocalDate.now().getYear();

            for (int i = 0; i < professions.size(); i++) {
                Profession profession = professions.get(i);
                int startYear = profession.getStartedYear();
                int startMonth = profession.getStartedMonth();
                int endYear = profession.getEndYear();
                int endMonth = profession.getEndMonth();

                int years = endYear - startYear;
                int months = endMonth - startMonth;

                if (months < 0) {
                    years--;
                    months += 12;
                }

                if (years > 0 || (years == 0 && startYear < currentYear)) {
                    totalMonths += (years * 12) + months;

                    // Check for overlapping experience in other professions
                    for (int j = i + 1; j < professions.size(); j++) {
                        Profession nextProfession = professions.get(j);
                        int nextStartYear = nextProfession.getStartedYear();
                        int nextStartMonth = nextProfession.getStartedMonth();

                        if (nextStartYear == startYear && nextStartMonth == startMonth) {
                            int nextEndYear = nextProfession.getEndYear();
                            int nextEndMonth = nextProfession.getEndMonth();
                            int overlapYears = nextEndYear - nextStartYear;
                            int overlapMonths = nextEndMonth - nextStartMonth;

                            if (overlapMonths < 0) {
                                overlapYears--;
                                overlapMonths += 12;
                            }

                            if (overlapYears > 0 || (overlapYears == 0 && nextStartYear < currentYear)) {
                                totalMonths -= (overlapYears * 12) + overlapMonths;
                            }
                        }
                    }
                }
            }

            int experienceInYears = (totalMonths / 12)+1;
            System.out.println("\n experienceInYears: " + experienceInYears);

            if (experienceInYears < 1) {
                return "до 1 года";
            } else if (experienceInYears >= 1 && experienceInYears < 3) {
                return "от 1 - 3 лет";
            } else if (experienceInYears >= 3 && experienceInYears < 6) {
                return "от 3 - 6 лет";
            } else {
                return "от 6 лет";
            }
        }


    }
