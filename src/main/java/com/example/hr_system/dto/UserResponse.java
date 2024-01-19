package com.example.hr_system.dto;


import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String companyName;
    private String email;
    private int unReadMessages;
    private Role role;
    private boolean isOnline;
    private FileResponse fileResponse;
}
