package com.example.hr_system.controller;


import com.example.hr_system.dto.cand.EducationDto;
import com.example.hr_system.dto.employer.EmployerRequests;
import com.example.hr_system.dto.employer.EmployerResponses;
import com.example.hr_system.dto.notification.NotificationResponse;
import com.example.hr_system.entities.*;
import com.example.hr_system.enums.*;
import com.example.hr_system.exception.BlockedException;
import com.example.hr_system.exception.NotFoundException;
import com.example.hr_system.repository.*;
import com.example.hr_system.service.BlockedUserService;
import com.example.hr_system.service.EmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("/employer")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class EmployerController {
    private final EmployerService employerService;
    private final UserRepository userRepository;
    private final BlockedUserService blockedUserService;

    @GetMapping("profile/{id}")
    public EmployerResponses getEmployerById(@PathVariable Long id,
                                             @RequestHeader (name="Authorization") String token) {
        if (blockedUserService.getUsernameFromToken(token).getBlockedUser().getViewingCompanyData()) {
            throw new BlockedException("THIS USER IS NOT PROHIBITED FROM VIEWING COMPANY DATA");
        }
        return employerService.getById(id);
    }

    @PostMapping("update/employer/{id}")
    public EmployerResponses updateEmployer(@PathVariable Long id, @RequestBody EmployerRequests employerRequests) {
        return employerService.update(id, employerRequests);
    }



    @GetMapping("/educations")
    public List<EducationDto> education() {
        return employerService.getEducations();
    }

    @GetMapping("employers")
    public List<EmployerResponses> getAllEmployers() {
        return employerService.getAll();
    }


    @GetMapping("/typeofEmployments")
    public List<String> responseEntity() {
        return employerService.getTypeOfEmployments();
    }


    @GetMapping("/byApplicationDate")
    public ApplicationDate[] applicationDates() {
        return ApplicationDate.values();
    }

    // IMAGE AND FILE UPLOAD

    @GetMapping("/salaryType")
    public List<String> salaryType() {
        return employerService.getSalaryTypes();
    }

    @GetMapping("getValute")
    public List<String> valute() {
        return employerService.getValutes();
    }

    @GetMapping("/notifications/{userId}")
    public List<NotificationResponse> findAllNotificationsByUserId(@PathVariable Long userId) {
        return employerService.findAllNotificationsByUserId(userId);
    }
}

