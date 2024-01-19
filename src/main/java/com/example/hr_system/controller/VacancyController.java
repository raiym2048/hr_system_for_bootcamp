package com.example.hr_system.controller;


import com.example.hr_system.config.MyHandler;
import com.example.hr_system.dto.JobSeekerVacanciesResponses;
import com.example.hr_system.dto.cand.EducationDto;
import com.example.hr_system.dto.image.Response;
import com.example.hr_system.dto.message.TypeResponse;
import com.example.hr_system.dto.vacancy.*;
import com.example.hr_system.entities.JobSeeker;
import com.example.hr_system.entities.JobSeekerVacancyInformation;
import com.example.hr_system.entities.Notification;
import com.example.hr_system.entities.User;
import com.example.hr_system.enums.Education;
import com.example.hr_system.enums.StatusOfJobSeeker;
import com.example.hr_system.enums.StatusOfVacancy;
import com.example.hr_system.exception.BlockedException;
import com.example.hr_system.exception.NotFoundException;
import com.example.hr_system.mapper.NotificationMapper;
import com.example.hr_system.mapper.VacancyMapper;
import com.example.hr_system.repository.*;
import com.example.hr_system.security.JwtTokenProvider;
import com.example.hr_system.service.BlockedUserService;
import com.example.hr_system.service.impl.VacancyServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vacancy")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class VacancyController {


    private final VacancyServiceImpl vacancyService;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;
    private final VacancyMapper vacancyMapper;
    private final VacancyRepository vacancyRepository;
    private final BlockedUserService blockedUserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JobSeekerVacancyInformationRepository jobSeekerVacancyInformationRepository;
    private final MyHandler myHandler;
    private final NotificationRepository notificationRepository;

    @PostMapping("/sendNotification")
    public void sendMessage(@RequestHeader("Authorization") String token){
        myHandler.notificationSend2(blockedUserService.getUsernameFromToken(token), notificationRepository.findById(1L).get());
    }


    @GetMapping("/vacancies")
    public List<VacancyResponse> all(@RequestHeader(name = "Authorization") String token) {
        if (!token.isEmpty())
            if (blockedUserService.getUsernameFromToken(token).getBlockedUser().getViewingAndSearchingForVacancies()) {
                throw new BlockedException("THIS USER IS PROHIBITED FROM VIEWING AND SEARCHING FOR VACANCIES");
            }
        return vacancyMapper.toDtos(vacancyRepository.findAll());
    }

    @GetMapping("/statusOfVacancy")
    public List<EducationDto> status() {
        List<EducationDto> strings = new ArrayList<>();
        for (StatusOfVacancy education : StatusOfVacancy.values()) {
            EducationDto dto = new EducationDto();
            dto.setName(education.getRussianTranslation());
            strings.add(dto);
        }
        return strings;
    }


    @GetMapping("/vacancy/{vacancyId}")
    public VacancyResponse getByIdVacancy(@PathVariable Long vacancyId,
                                          @RequestHeader(name = "Authorization") String token) {

        if (!token.isEmpty())
            if (blockedUserService.getUsernameFromToken(token).getBlockedUser().getViewingAndSearchingForVacancies()) {
                throw new BlockedException("THIS USER IS PROHIBITED FROM VIEWING AND SEARCHING FOR VACANCIES");
            }

        return vacancyMapper.toDto(vacancyRepository.findById(vacancyId).orElseThrow(() ->
                new NotFoundException("vacancy not found with id: " + vacancyId,HttpStatus.NOT_FOUND)));
    }

    @PostMapping("/vacancy/{employerId}")
    public VacancyResponse save(@PathVariable Long employerId,
                                @RequestBody VacancyRequest vacancyRequest) {
        return vacancyService.saveVacancy(employerId, vacancyRequest);
    }


    @DeleteMapping("/delete/{vacancyId}")
    public boolean delete(@PathVariable("vacancyId") Long vacancyId) {
       return vacancyService.delete(vacancyId);
    }

    @GetMapping("/vacancy/search")
    public List<JobSeekerVacanciesResponses> vacancySearch(@RequestParam(required = false) String search,
                                                           @RequestHeader(name = "Authorization") String token) {
        System.out.println("the token:" + token + "1");
        return vacancyService.searchVacancy(search);
    }
    @GetMapping("/search")
    public List<JobSeekerVacanciesResponses> vacancySearch2(@RequestParam(required = false) String search) {

        return vacancyService.searchVacancy(search);
    }



    @GetMapping("/vacancy/filter")
    public List<JobSeekerVacanciesResponses> filter(@RequestParam(required = false) String category, @RequestParam(required = false) String position, @RequestParam(required = false) String country,
                                                    @RequestParam(required = false) String city, @RequestParam(required = false) String experience, @RequestParam(required = false)
                                                    String typeOfEmployments, @RequestParam(required = false) Boolean salary,
                                                    @RequestParam(required = false) Boolean date) {


        return vacancyService.filter(category, position, country, city, experience, typeOfEmployments, salary, date);
    }

    @GetMapping("/employer/vacancies/search/{userId}")
    List<VacancyResponse> employerVacanciesSearchUserId(@PathVariable Long userId,
                                                        @RequestParam(required = false) String search,
                                                        @RequestHeader(name = "Authorization") String token) {
        if (blockedUserService.getUsernameFromToken(token).getBlockedUser().getViewingAndSearchingForVacancies()) {
            throw new BlockedException("THIS USER IS PROHIBITED FROM VIEWING AND SEARCHING FOR VACANCIES");
        }
        return vacancyService.employerVacanciesSearchUserId(userId, search);
    }

    @GetMapping("/employer/vacancies/filter/{userId}")
    List<VacancyResponse> employerVacanciesFilterUserId(@PathVariable
                                                        Long userId, @RequestParam(required = false) String respondedCount,
                                                        @RequestParam(required = false) String byDate, @RequestParam
                                                                (required = false) String byStatusOfVacancy) {

        return vacancyService.employerVacanciesFilterUserId(userId, respondedCount, byDate, byStatusOfVacancy);
    }

    @PutMapping("/newStatusForVacancy/{vacancyId}")
    private void newStatus(@PathVariable Long vacancyId,
                           @RequestParam(required = false) String status,
                           @RequestHeader("Authorization") String token) {

        // Извлечение токена из тела запроса
        if (blockedUserService.getUsernameFromToken(token).getBlockedUser().getVacancyAndHiringManagement()) {
            throw new BlockedException("THIS USER IS NOT ALLOWED TO VACANCY AND HIRING MANAGEMENT");
        }
        // ... ваш текущий код


        vacancyService.setStatusOfVacancy(vacancyId, status);
    }


    @GetMapping("/vacancies/{userId}")
    public List<VacancyResponse> getAllMyVacancies(@PathVariable Long userId,
                                                   @RequestHeader(name = "Authorization") String token) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User not found!" + userId,HttpStatus.NOT_FOUND));
        Long employerId = user.getEmployer().getId();
        if (blockedUserService.getUsernameFromToken(token).getBlockedUser().getViewingAndSearchingForVacancies()) {
            throw new BlockedException("THIS USER IS PROHIBITED FROM VIEWING AND SEARCHING FOR VACANCIES");
        }
        return vacancyService.getMyVacancies(employerId);
    }

    @PostMapping("/update/vacancy/{vacancyId}")
    public VacancyResponse updateVacancy(@PathVariable Long vacancyId,
                                         @RequestBody VacancyRequest vacancyRequest,
                                         @RequestHeader(name = "Authorization", required = false) String token) {

        if (blockedUserService.getUsernameFromToken(token).getBlockedUser().getVacancyAndHiringManagement()) {
            throw new BlockedException("THIS USER IS NOT ALLOWED TO VACANCY AND HIRING MANAGEMENT");
        }
        return vacancyService.update(vacancyId, vacancyRequest);
    }

    @GetMapping("/list/responded/vacancy/{userId}")
    public List<RespondedVacancyResponse> respondedVacancyResponses(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        JobSeeker jobSeeker = user.getJobSeeker();
        return vacancyMapper.toListRespondedVacancyResponse(jobSeeker, jobSeeker.getVacancies());
    }


    @GetMapping("/aboutVacancy/{vacancyId}")
    AboutVacancy aboutVacancy(@PathVariable Long vacancyId,
                              @RequestHeader(name = "Authorization") String token) {
        if (!token.isEmpty())
            if (blockedUserService.getUsernameFromToken(token).getBlockedUser().getViewingAndSearchingForVacancies()) {
                throw new BlockedException("THIS USER IS PROHIBITED FROM VIEWING AND SEARCHING FOR VACANCIES");
            }
        return vacancyService.aboutVacancy(vacancyId);
    }

    @GetMapping("/sortedTwoSameVacancy/{vacancyId}")
    public List<VacancyResponse> sortedTwoVacancy(@PathVariable Long vacancyId) {
        return vacancyService.sortedTwoVacancies(vacancyId);
    }

    @GetMapping("list/job_seekers/responded/vacancies/search/{userId}")
    public List<RespondedVacancyResponse> resSearch(@PathVariable Long userId,
                                                    @RequestParam(required = false) String search) {
        User user = userRepository.findById(userId).orElseThrow();
        JobSeeker jobSeeker = user.getJobSeeker();
//        System.out.println(jobSeeker.getVacancies().size() + "\n\n\n");
        return vacancyMapper.toListRespondedVacancyResponse(jobSeeker, vacancyService.respondedJobseekersSearch(search));
    }

    @GetMapping("list/job_seekers/responded/vacancies/filter/{userId}")
    public List<RespondedVacancyResponse> filter(@PathVariable Long userId, @RequestParam(required = false) String days, @RequestParam
            (required = false) String statusOfJobSeeker) {
        User user = userRepository.findById(userId).orElseThrow();
        JobSeeker jobSeeker = user.getJobSeeker();
        System.out.println(jobSeeker.getVacancies().size() + "\n\n\n");

        return vacancyService.respondedJobseekersFilterByDaysAndStatus(days, jobSeeker, statusOfJobSeeker, userId);
    }
    @GetMapping("/statusOfvacancy/forJob_seeker")
    public boolean getStatusOfVacancyForJobSeeker(@RequestHeader("Authorization") String token, @RequestParam(required = false)
                                                          Long vacancyId) {
        User user = blockedUserService.getUsernameFromToken(token);
        JobSeekerVacancyInformation jobSeekerVacancyInformation =
                jobSeekerVacancyInformationRepository.findByJobSeekerIdAndVacancyId(user.getJobSeeker().getId(), vacancyId);
        if (jobSeekerVacancyInformation != null) {
            return true;
        } else {
            return false;
        }
    }
    @GetMapping("/popularCategory")
    public List<VacancyResponseForPopularCategories> getCategoryCounts() {
        return vacancyService.getVacancyCountByCategory();
    }
}



