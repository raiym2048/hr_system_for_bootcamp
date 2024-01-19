package com.example.hr_system.dto.employer;

import com.example.hr_system.dto.GenericResponseForUserResponses;
import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployerResponses extends GenericResponseForUserResponses {
    private String companyName;
    private String aboutCompany;
    private String country;
    private String city;
    private String address;
    private String email;
    private String phoneNumber;
    private Role role;
    private FileResponse fileResponse;
    private boolean isOnline;
    private Boolean viewingCandidateData;
    private Boolean vacancyAndHiringManagement;
    private Boolean communicationWithJobSeekers;
    private Boolean blocked;
}
