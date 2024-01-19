package com.example.hr_system.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsesForAdmin {
    private Long userId;
    private String userName;
    private String userRole;
    private String userEmail;
    private String lastVisit;
}
