package com.example.hr_system.service;

import com.example.hr_system.dto.auth.AuthenticationRequest;
import com.example.hr_system.dto.auth.AuthenticationResponse;
import com.example.hr_system.dto.employer.RegisterEmployerRequest;
import com.example.hr_system.dto.jobSeeker.RegisterJobSeekerRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);

    void validationRegister(String email, String password);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    ResponseEntity<AuthenticationResponse> jobSeekerRegister(RegisterJobSeekerRequest request);

    ResponseEntity<AuthenticationResponse> adminRegister(RegisterJobSeekerRequest request);

    ResponseEntity<AuthenticationResponse> employerRegister(RegisterEmployerRequest request);

    void googleTokenAuth(String token2, String role);

    void googleTokenLogin(String email);
}
