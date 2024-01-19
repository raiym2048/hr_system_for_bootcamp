package com.example.hr_system.dto.jobSeeker;


import com.example.hr_system.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobSeekerResponse {

    private Long id;

    private String firstname;
    private String lastname;
    private Role role;


}
