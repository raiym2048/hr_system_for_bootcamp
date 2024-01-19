package com.example.hr_system.dto.employer;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployerRequests {

    private String aboutCompany;
    private String companyName;
    private String country;
    private String city;
    private String address;
    private String email;
    private String phoneNumber;
}
