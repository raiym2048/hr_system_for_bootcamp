package com.example.hr_system.dto.employer;

import com.example.hr_system.dto.image.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployerResponse {
    private Long id;
    private Long imageId;
    private String companyName;
}
