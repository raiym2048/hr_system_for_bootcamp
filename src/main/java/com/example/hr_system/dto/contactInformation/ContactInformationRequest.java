package com.example.hr_system.dto.contactInformation;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactInformationRequest {
    private String country;
    private String city;
    private String street_house;
}
