package com.example.hr_system.service.impl;

import com.example.hr_system.config.MyHandler;
import com.example.hr_system.dto.UserResponse;
import com.example.hr_system.dto.auth.AuthenticationRequest;
import com.example.hr_system.dto.auth.AuthenticationResponse;
import com.example.hr_system.dto.employer.RegisterEmployerRequest;
import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.dto.jobSeeker.RegisterJobSeekerRequest;
import com.example.hr_system.entities.*;
import com.example.hr_system.enums.Role;
import com.example.hr_system.exception.BadRequestException;
import com.example.hr_system.exception.BlockedException;
import com.example.hr_system.exception.CustomException;
import com.example.hr_system.mapper.FileMapper;
import com.example.hr_system.repository.*;
import com.example.hr_system.security.JwtTokenProvider;
import com.example.hr_system.service.AdminService;
import com.example.hr_system.service.AuthenticationService;
import com.example.hr_system.service.FileDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerRepository employerRepository;
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;


    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            String token = jwtTokenProvider.createToken(request.getEmail(), userRepository.findByEmail(request.getEmail()).get().getRole());
            return getAuthResponse(request.getEmail(), token);
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    @Override
    public void validationRegister(String email, String password) {
        if (!email.contains("@")) {
            throw new BadCredentialsException("invalid email!");
        }
        if (password.length() < 8) {
            throw new BadCredentialsException("invalid password!");
        }
    }

    private AuthenticationResponse getAuthResponse(String email, String token) {
        User auth = getAuth(email);
        AuthenticationResponse response = new AuthenticationResponse();
        UserResponse userResponse = new UserResponse();
        userResponse.setId(auth.getId());
        userResponse.setRole(auth.getRole());
        userResponse.setEmail(auth.getEmail());
        response.setUser(userResponse);
        response.setAccessToken(token);
        return response;
    }

    private User getAuth(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException("User not defined!", HttpStatus.NOT_FOUND);
        }

        return user.get();
    }
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Optional<User> optionalAuth = userRepository.findByEmail(request.getEmail());
        if (optionalAuth.isEmpty()) {
            throw new NotFoundException("User not found with email: " + request.getEmail());
        }

        User auth = optionalAuth.get();

        auth.setLastVisit(getCurrentTimeAsString());
        userRepository.save(auth);

        if (auth.getRole() != Role.ADMIN && auth.getBlocked()) {
            throw new BlockedException("THIS USER IS BLOCKED");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()));
        } catch (AuthenticationException e) {
            // Обработка ошибки аутентификации, например, неверный email или пароль
            throw new BadCredentialsException("Authentication failed: " + e.getMessage() + request.getEmail());
        }

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        String token = jwtTokenProvider.createToken(user.getEmail(), userRepository.findByEmail(user.getEmail()).get().getRole());
//        Optional<UserMessageInfo> userMessageInfo = userMessageInfoRepository.findByEmail(user.getEmail());
//        List<Message> messages = messageRepository.findAllBySender(user.getEmail());

// Obtain WebSocket session for the authenticated user

        return AuthenticationResponse.builder()
                .user(convertToresponse(user))
                .accessToken(token)
                .build();
    }


    @Override
    public ResponseEntity<AuthenticationResponse> jobSeekerRegister(RegisterJobSeekerRequest request) {
        checkUsernameIsExist(request.getEmail());
        User user = new User();
        if (request.getEmail().contains("@")) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setEmail(user.getEmail() != null ? user.getEmail() : null);
        if (request.getFirstname() != null) {
            jobSeeker.setFirstname(request.getFirstname());
        }
        if (request.getLastname() != null) {
            jobSeeker.setLastname(request.getLastname());
        }
        jobSeekerRepository.save(jobSeeker);

        user.setRole(Role.JOB_SEEKER);
        user.setJobSeeker(jobSeeker);
        BlockedUser blockedUser = new BlockedUser();

        blockedUser.setViewingCompanyData(false);
        blockedUser.setViewingAndSearchingForVacancies(false);
        blockedUser.setViewTheStatusOfResponded(false);
        blockedUser.setCommunicationWithEmployers(false);
        blockedUser.setViewingCandidateData(false);
        blockedUser.setVacancyAndHiringManagement(false);
        blockedUser.setCommunicationWithJobSeekers(false);
        user.setBlocked(false);
        user.setBlockedUser(blockedUser);
        userRepository.save(user);
        return ResponseEntity.ok(convertAuthentication(user));
    }


    @Override
    public ResponseEntity<AuthenticationResponse> adminRegister(RegisterJobSeekerRequest request) {
        checkUsernameIsExist(request.getEmail());
        User user = new User();
        if (request.getEmail().contains("@")) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        return ResponseEntity.ok(convertAuthentication(user));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> employerRegister(RegisterEmployerRequest request) {
        checkUsernameIsExist(request.getEmail());
        User user = new User();
        if (request.getEmail().contains("@")) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        Employer employer = new Employer();
        employer.setEmail(user.getEmail());
        if (request.getCompanyName() != null) {
            employer.setCompanyName(request.getCompanyName());
        }
        user.setEmployer(employer);
        employerRepository.save(employer);

        user.setRole(Role.EMPLOYER);
        BlockedUser blockedUser = new BlockedUser();

        blockedUser.setViewingCompanyData(false);
        blockedUser.setViewingAndSearchingForVacancies(false);
        blockedUser.setViewTheStatusOfResponded(false);
        blockedUser.setCommunicationWithEmployers(false);
        blockedUser.setViewingCandidateData(false);
        blockedUser.setVacancyAndHiringManagement(false);
        blockedUser.setCommunicationWithJobSeekers(false);
        user.setBlocked(false);
        user.setBlockedUser(blockedUser);
        userRepository.save(user);
        return ResponseEntity.ok(convertAuthentication(user));
    }

    private void checkUsernameIsExist(String email) {
        Optional<User> userEmail = userRepository.findByEmail(email);
        if (userEmail.isPresent()) {
            log.error("This email = {} already exist.Please choose another one", email);
            throw new CustomException(
                    String.format("This email = %s already exist.Please choose another one", email),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    private UserResponse convertToresponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setOnline(user.isOnline());
        userResponse.setCompanyName(user.getRole().equals(Role.EMPLOYER)? user.getEmployer().getCompanyName():null);
        userResponse.setId(user.getId());
        userResponse.setFirstname(user.getFirstname());
        userResponse.setLastname(user.getLastname());
        userResponse.setEmail(user.getEmail());
        userResponse.setUnReadMessages(user.getUnReadMessages());
        userResponse.setRole(user.getRole());
        Optional<FileData> fileData = null;
        if (user.getRole().equals(Role.JOB_SEEKER)){
        if (user.getJobSeeker().getImage()==null)
            return userResponse;

            fileData = fileRepository.findById(user.getJobSeeker().getImage().getId()).isPresent()?fileRepository.findById(user.getJobSeeker().getImage().getId()):null;
        } else if (user.getRole().equals(Role.EMPLOYER)) {
            if (user.getEmployer().getResume()==null)
                return userResponse;
            fileData = fileRepository.findById(user.getEmployer().getResume().getId()).isPresent()?fileRepository.findById(user.getEmployer().getResume().getId()):null;
        }
        if (!user.getRole().equals(Role.ADMIN))
            userResponse.setFileResponse(fileMapper.toDto(fileData.isPresent()?fileData.get():null));

        return userResponse;
    }

    private AuthenticationResponse convertAuthentication(User user) {
        AuthenticationResponse response = new AuthenticationResponse();

        response.setUser(convertToresponse(user));
        String token = jwtTokenProvider.createToken(user.getEmail(), userRepository.findByEmail(user.getEmail()).get().getRole());
        response.setAccessToken(token);
        return response;
    }


    @Override
    public void googleTokenAuth(String token2, String role) {
        String[] chunks = token2.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));

        int first = payload.indexOf("email") + 8;
        int second = payload.indexOf("\",\"email_verified");
        String email = payload.substring(first, second);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            googleTokenRegister(email, role);
        } else {
            googleTokenLogin(email);
        }

        //
    }

    @Override
    public void googleTokenLogin(String email) {
        User user = userRepository.findByEmail(email).get();
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(), // Use the email as the username
                "", // Empty password since we don't need it for token-based authentication
                user.getAuthorities() // Get the user's roles/authorities
        );

        // Create an Authentication token
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null, // No credentials for token-based authentication
                userDetails.getAuthorities()
        );

        // Authenticate the user
        SecurityContextHolder.getContext().setAuthentication(authentication);


        //how to authenticate the user, for accessing security
    }

    private void googleTokenRegister(String email, String role) {
        User user = new User();
        user.setEmail(email);
        user.setRegisteredFromGoogle(true);
        user.setRole(Role.valueOf(role));
        userRepository.save(user);

    }

    private String getCurrentTimeAsString() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, HH:mm");
        return now.format(formatter);
    }
}
