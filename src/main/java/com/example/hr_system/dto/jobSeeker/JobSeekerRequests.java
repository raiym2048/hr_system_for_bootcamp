package com.example.hr_system.dto.jobSeeker;


import com.example.hr_system.dto.education.EducationRequest;
import com.example.hr_system.dto.profession.ProfessionRequest;
import com.example.hr_system.enums.Education;
import com.example.hr_system.enums.Month;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobSeekerRequests {

    private Long imageId;
    private String firstname;
    private String lastname;
    private LocalDate birthday;

    private String country;
    private String city;
    private String address;
    private String phoneNumber;
    private String about;
    List<EducationRequest> educationRequests;
    List<ProfessionRequest> professionRequests;



    private boolean untilNow;


    private Long resumeId;


}
