package com.example.hr_system.dto.employer;

import com.example.hr_system.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployerRequest {
    private String companyName;
    private String email;
    private String password;
    private Role role;

}
