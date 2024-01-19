package com.example.hr_system.dto.vacancy;


import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.entities.FileData;
import com.example.hr_system.enums.StatusOfJobSeeker;
import com.example.hr_system.enums.StatusOfVacancy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RespondedVacancyResponse {
    private Long vacancyId;
    private FileResponse image;
    private String companyName;
    private String position;
    private String category;
    private String applicationDate;
    private StatusOfJobSeeker statusOfJobSeeker;

}
