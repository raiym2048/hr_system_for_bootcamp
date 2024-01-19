package com.example.hr_system.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockedUserRequest {
    private Boolean viewingCompanyData;
    private Boolean viewingAndSearchingForVacancies;
    private Boolean viewTheStatusOfResponded;
    private Boolean communicationWithEmployers;
    private Boolean viewingCandidateData;
    private Boolean vacancyAndHiringManagement;
    private Boolean communicationWithJobSeekers;
}
