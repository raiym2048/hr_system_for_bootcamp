package com.example.hr_system.dto.jobSeeker;

import com.example.hr_system.dto.GenericResponseForUserResponses;
import com.example.hr_system.dto.education.EducationResponse;
import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.dto.profession.ProfessionResponse;
import com.example.hr_system.enums.Education;
import com.example.hr_system.enums.Month;
import com.example.hr_system.enums.Role;
import com.example.hr_system.enums.StatusOfJobSeeker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerResponses extends GenericResponseForUserResponses {
    private FileResponse imageResponse;
    private String firstname;
    private String lastname;
    private String about;
    private FileResponse resumeResponse;
    private LocalDate birthday;
    private String country;
    private String city;
    private String address;
    private String email;
    private String phoneNumber;
    private Role role;
    private StatusOfJobSeeker statusOfJobSeeker;
    private List<EducationResponse> educationResponse;
    private List<ProfessionResponse>  professionResponse;
    private boolean isOnline;
    private Boolean viewingCompanyData;
    private Boolean viewingAndSearchingForVacancies;
    private Boolean viewTheStatusOfResponded;
    private Boolean communicationWithEmployers;
    private Boolean blocked;
}
