package com.example.hr_system.dto.employer;


import lombok.Data;

@Data
public class RegisterEmployerRequest {
    private String companyName;
    private String email;
    private String password;
}
