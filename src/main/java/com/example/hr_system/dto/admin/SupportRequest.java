package com.example.hr_system.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupportRequest {
    private String personName;
    private String personEmail;
    private Integer personPhoneNumber;
    private String massage;
}
