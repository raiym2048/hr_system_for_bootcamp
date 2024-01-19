package com.example.hr_system.controller;

import com.example.hr_system.dto.auth.AuthenticationRequest;
import com.example.hr_system.dto.employer.RegisterEmployerRequest;
import com.example.hr_system.dto.jobSeeker.RegisterJobSeekerRequest;
import com.example.hr_system.service.AuthenticationService;
import com.example.hr_system.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {
    private final AuthenticationService service;

    private final UserService userService;

    @PostMapping("/register/job")
    public ResponseEntity<?> jobSeekerRegister(@RequestBody RegisterJobSeekerRequest request) {
        return service.jobSeekerRegister(request);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> adminRegister(@RequestBody RegisterJobSeekerRequest request) {
        return service.adminRegister(request);
    }

    @PostMapping("/register/emp")
    public ResponseEntity<?> employerRegister(@RequestBody RegisterEmployerRequest request) {
        return service.employerRegister(request);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("email_sender/send")
    public boolean sender(@RequestParam String email) {
        return userService.sender(email);
    }

    @PostMapping("/email_sender/check")
    public boolean checkingCode(@RequestParam int code) {
        return userService.checkingCode(code);
    }

    @PostMapping("/email_sender/changePassword")
    public boolean changePassword(@RequestParam String email, @RequestParam String password) {
        return userService.changePassword(email, password);
    }
}
