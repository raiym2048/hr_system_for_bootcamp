package com.example.hr_system.dto.contactInformation;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactInformationResponse {
    private Long idl;

    private String country;
    private String city;
    private String street_house;
}
