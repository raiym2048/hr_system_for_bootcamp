package com.example.hr_system.service.impl;

import com.example.hr_system.dto.GenericResponseForUserResponses;
import com.example.hr_system.dto.admin.ResponsesForAdmin;
import com.example.hr_system.dto.admin.ResponsesForSupport;
import com.example.hr_system.dto.admin.SupportRequest;
import com.example.hr_system.entities.*;
import com.example.hr_system.enums.Role;
import com.example.hr_system.exception.BlockedException;
import com.example.hr_system.mapper.*;
import com.example.hr_system.repository.*;
import com.example.hr_system.service.AdminService;
import com.example.hr_system.service.EmployerService;
import com.example.hr_system.service.VacancyService;
import com.example.hr_system.utils.DateTimeUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;
    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerMapper employerMapper;
    private final JobSeekerMapper jobSeekerMapper;
    private final PasswordEncoder passwordEncoder;
    private final SupportMapper supportMapper;
    private final ListSupportRepository listSupportRepository;
    private final EmployerService employerService;
    private final VacancyRepository vacancyRepository;
    private final VacancyService vacancyService;
    private final NotificationRepository notificationRepository;

    @Override
    public List<ResponsesForAdmin> getAllUsers(String name, String userRole) {

        if (name == null || name.isEmpty() && userRole.isEmpty() || userRole == null) {
            return adminMapper.toDtos(userRepository.findEmployersAndJobSeekers(
                    List.of(Role.EMPLOYER, Role.JOB_SEEKER)));
        } else if (!name.isEmpty() && userRole.isEmpty()) {
            return adminMapper.toDtos(userRepository.searchByFirstNameLastNameAndCompanyName(name));
        } else {
            return adminMapper.toDtos(userRepository.searchByNameAndRoleSortedByNameAndRole(name, Role.valueOf(userRole)));
        }
    }

    @Override
    public GenericResponseForUserResponses getResponsesForUserById(Long userId) {
        String userRole = userRepository.findById(userId).get().getJobSeeker() == null ? Role.EMPLOYER.name() : Role.JOB_SEEKER.name();
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User"));
        if (userRole.equals(Role.EMPLOYER.name())) {

            return employerMapper.toDto(employerRepository.findById(user.getEmployer().getId()).orElseThrow(() ->
                    new NotFoundException("we don't have employer with id :" + userId)));
        } else if (userRole.equals(Role.JOB_SEEKER.name())) {
            return jobSeekerMapper.toDto(jobSeekerRepository.findById(user.getJobSeeker().getId()).orElseThrow(() ->
                    new NotFoundException("not found jobseeker with id: " + userId)));
        } else {
            throw new IllegalArgumentException("Invalid user type: " + userRole);
        }
    }

    @Override
    public GenericResponseForUserResponses setRoleForUser(Long userId, String role) {
        boolean isEmployer = false;
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No User"));
        if (role.equals(Role.EMPLOYER.name()) && !user.getRole().name().equals(Role.EMPLOYER.name())) {
            isEmployer = true;
            if (user.getJobSeeker() != null) {
                Employer employer = dublicateEmployeer(user.getJobSeeker());
                user.setRole(Role.EMPLOYER);
                JobSeeker jobSeeker = user.getJobSeeker();
                user.setJobSeeker(null);
                user.setEmployer(employer);
                employerRepository.save(employer);
                jobSeeker.setUser(null);
                jobSeekerRepository.save(jobSeeker);
                userRepository.save(user);
                jobSeekerRepository.delete(jobSeeker);
            }
        } else if (role.equals(Role.JOB_SEEKER.name()) && !user.getRole().name().equals(Role.JOB_SEEKER.name())) {
            isEmployer = false;
            if (user.getEmployer() != null) {
                JobSeeker jobSeeker = dublicateJobSeeker(user.getEmployer());
                user.setRole(Role.JOB_SEEKER);
                Employer employer = user.getEmployer();
                user.setEmployer(null);
                user.setJobSeeker(jobSeeker);
                jobSeekerRepository.save(jobSeeker);
                employer.setUser(null);
                employerRepository.save(employer);
                userRepository.save(user);
                employerRepository.delete(employer);
            }
        } else {
            throw new IllegalArgumentException("Invalid user type: " + role);
        }
        return !isEmployer ? jobSeekerMapper.toDto(jobSeekerRepository.findById(user.getJobSeeker().getId()).orElseThrow()) :
                employerMapper.toDto(employerRepository.findById(user.getEmployer().getId()).orElseThrow(() ->
                        new NotFoundException("we don't have employer with id :" + userId)));
    }

    @Override
    public boolean deleteByAccount(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new com.example.hr_system.exception.NotFoundException("The email is not correct: " + email, HttpStatus.NOT_FOUND));
        delete(user);
        return true;
    }

    @Override
    public boolean deleteByAccount(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new com.example.hr_system.exception.NotFoundException("The email is not correct: " + email, HttpStatus.NOT_FOUND));
        return delete(user, password);
    }

    public void delete(User user) {
        if (user.getEmployer() != null) {
            Employer employer = user.getEmployer();
            employer.setUser(null);
            employerRepository.delete(employer);
        } else {
            JobSeeker jobSeeker = user.getJobSeeker();
            jobSeeker.setUser(null);
            jobSeekerRepository.delete(jobSeeker);
        }
//        userRepository.delete(user);

//        if (userRepository.existsById(user.getId())) {
//            throw new RuntimeException("User with ID " + user.getId() + " does not exist.");
//        }
    }

    public boolean delete(User user, String password) {
        if (passwordEncoder.matches(password, user.getPassword())) {
            if (user.getEmployer() != null) {
                Employer employer = user.getEmployer();
                employer.setUser(null);
                employerRepository.delete(employer);
            } else {
                JobSeeker jobSeeker = user.getJobSeeker();

                jobSeeker.setUser(null);
                jobSeekerRepository.delete(jobSeeker);
            }
            userRepository.delete(user);

        } else {
            throw new BadCredentialsException("false password!");
        }

        if (userRepository.existsById(user.getId())) {
            throw new RuntimeException("User with ID " + user.getId() + " does not exist.");
        }
        return true;
    }

    private Employer dublicateEmployeer(JobSeeker jobSeeker) {
        Employer employer = new Employer();
        employer.setCompanyName(jobSeeker.getFirstname());
        employer.setEmail(jobSeeker.getEmail());
        employer.setPassword(jobSeeker.getPassword());
        employer.setAddress(jobSeeker.getAddress());
        employer.setPhoneNumber(jobSeeker.getPhoneNumber());
        employer.setCity(jobSeeker.getCity());
        employer.setResume(null);
        employer.setCountry(jobSeeker.getCountry());
        employer.setUser(jobSeeker.getUser());
        employer.setFavorites(null);
        employer.setVacancyList(null);
        employer.setAboutCompany(null);
        return employer;
    }

    private JobSeeker dublicateJobSeeker(Employer employer) {
        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setUser(employer.getUser());
        jobSeeker.setBirthday(null);
        jobSeeker.setCountry(employer.getCountry() == null ? null : employer.getCountry());
        jobSeeker.setCity(employer.getCity());
        jobSeeker.setAddress(employer.getAddress());
        jobSeeker.setStatusOfJobSeeker(null);
        jobSeeker.setUserApplicationDate(null);
        jobSeeker.setPhoneNumber(employer.getPhoneNumber());
        jobSeeker.setAbout(null);
        jobSeeker.setEducationJobs(new ArrayList<>());
        jobSeeker.setFirstname(employer.getCompanyName());
        jobSeeker.setLastname(null);
        jobSeeker.setEmail(employer.getEmail());
        jobSeeker.setPassword(employer.getPassword());
        jobSeeker.setProfessions(new ArrayList<>());
        jobSeeker.setResume(null);
        jobSeeker.setVacancies(new ArrayList<>());
        jobSeeker.setRole(Role.JOB_SEEKER);
        jobSeeker.setEmployers(new ArrayList<>());
        jobSeeker.setIsFavorite(null);
        return jobSeeker;

    }

    @Override
    public boolean listForDeletingUsers(List<Long> selectedUserIds) {
        for (Long userId : selectedUserIds) {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent())
                delete(user.get());
            userRepository.deleteById(userId);
            if (userRepository.findById(userId).isPresent()){
                System.out.println(111111111);
            }
            else {
                System.out.println(99999999);
            }

        }
        return true;
    }

    @Override
    public boolean listForDeletingVacancy(List<Long> selectedVacancyIds) {
        System.out.println("LIST VACANCY IDs - "+selectedVacancyIds.toString());
        for (Long vacancyId : selectedVacancyIds) {
           Optional<Vacancy> vacancy = vacancyRepository.findById(vacancyId);
            System.out.println("deleting "+ vacancyId);
            vacancyService.delete(vacancyId);
        }
        return true;
    }

    @Override
    public boolean userBlocked(Long userId, Boolean aBoolean) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setBlocked(aBoolean);
        userRepository.save(user);
        return user.getBlocked();
    }

    @Override
    public void block(String email) {
        Optional<User> auth = userRepository.findByEmail(email);
        if (auth.isPresent()) {
            if (auth.get().getBlocked()) {
                throw new BlockedException("THIS USER IS BLOCKED");
            }
        }

    }

    @Override
    public User isAuth(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication;
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() ->
                new com.example.hr_system.exception.NotFoundException("The user = %s is blocked!" + userDetails.getUsername(), HttpStatus.NOT_FOUND));
    }

    @Override
    public List<ResponsesForSupport> getListSupport() {
        return supportMapper.toDtos(listSupportRepository.findAll());
    }

    @Override
    public ResponsesForSupport createSupport(SupportRequest request) {
        ListSupport listSupport = new ListSupport();
        listSupport.setPersonName(request.getPersonName());
        listSupport.setPersonEmail(request.getPersonEmail());
        listSupport.setPersonPhoneNumber(request.getPersonPhoneNumber());
        listSupport.setMessage(request.getMassage());
        listSupport.setDateSent(DateTimeUtil.getCurrentDateTime());
        listSupportRepository.save(listSupport);
        return supportMapper.toDto(listSupport);
    }

    @Override
    public boolean deleteSupportById(Long supportId) {
        try {
            if (listSupportRepository.existsById(supportId)) {
                listSupportRepository.deleteById(supportId);
                return true;
            } else return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}
