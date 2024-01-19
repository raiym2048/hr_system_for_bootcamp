package com.example.hr_system.controller;

import com.example.hr_system.dto.jobSeeker.*;
import com.example.hr_system.dto.notification.NotificationResponse;
import com.example.hr_system.dto.vacancy.VacancyResponse;
import com.example.hr_system.entities.Vacancy;
import com.example.hr_system.exception.BlockedException;
import com.example.hr_system.mapper.JobSeekerMapper;
import com.example.hr_system.repository.JobSeekerRepository;
import com.example.hr_system.repository.UserRepository;
import com.example.hr_system.service.BlockedUserService;
import com.example.hr_system.service.EmployerService;
import com.example.hr_system.service.JobSeekerService;
import com.example.hr_system.service.VacancyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("job_seeker/")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class JobSeekerController {
    private final JobSeekerService jobSeekerService;
    private final VacancyService vacancyService;
    private final UserRepository userRepository;
    public final JobSeekerMapper jobSeekerMapper;
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerService employerService;
    private final BlockedUserService blockedUserService;


    @GetMapping("/candidate/favorites/{userId}")
    public List<CandidateResponses> getEmployerFavorites(@PathVariable Long userId) {
        return employerService.favoriteCandidateResponses(userId);
    }


    @GetMapping("/search")
    public List<CandidateResponses> search(@RequestParam(required = false) String name, @RequestParam(required = false) String email) {
        System.out.println("nameis"+name+"1");
        return jobSeekerService.findByName(name, email);
    }


    @GetMapping("/candidate/filter")
    public List<CandidateResponses> filter2(@RequestParam(required = false) String position,
                                            @RequestParam(required = false) String education,
                                            @RequestParam(required = false) String country,
                                            @RequestParam(required = false) String city,
                                            @RequestParam(required = false) String experience) {
        return jobSeekerService.filter2(position,education,country,city,experience);
    }

    @GetMapping("/job_seekers")
    public List<JobSeekerResponses> getAllJobSeekers() {
        return jobSeekerService.getAllJobSeekers();
    }

    @PutMapping("/setStatusForJobSeeker/{vacancyId}/{userId}")
    public void setStatusForJobSeeker(@PathVariable Long vacancyId,
                                      @PathVariable Long userId,
                                      @RequestParam(required = false) String status) {

//        if (blockedUserService.getUsernameFromToken(token).getBlockedUser().getVacancyAndHiringManagement()) {
//            throw new BlockedException("THIS USER IS NOT ALLOWED TO VACANCY AND HIRING MANAGEMENT");
//        }
        vacancyService.setStatusOfJobSeeker(vacancyId, userId, status);
    }

    @GetMapping("/list/responded/{vacancyId}")
    public List<RespondedResponse> responded(@PathVariable Long vacancyId) {
        return vacancyService.listForResponded(vacancyId);
    }
    @GetMapping("/list/responded/forEmployer/{vacancyId}")
    public List<RespondedResponse> respondedForEmployer(@PathVariable Long vacancyId) {
        return vacancyService.listForResponded(vacancyId);
    }

    @GetMapping("/list/responded/filter/{vacancyId}")
    public List<RespondedResponse> filter(@PathVariable Long vacancyId, @RequestParam(required = false) String statusOfJobSeeker,@RequestParam(required = false) String experience,@RequestParam(required = false) String localDate) {

        return vacancyService.listForResponded(vacancyId, employerService.containsStatusOfJobSeeker(statusOfJobSeeker) ? statusOfJobSeeker : null, experience, localDate);
    }

    @GetMapping("/get/job_seeker/myId/{userId}")
    public JobSeekerResponses jobSeekerResponsesUser(@PathVariable Long userId) {
        return jobSeekerService.getById2(userId);
    }

    @GetMapping("/list/responded/search/{vacancyId}")
    public List<RespondedResponse> searchResponded(@PathVariable Long vacancyId, @RequestParam(required = false) String names) {

        return vacancyService.listForResponded(vacancyId, jobSeekerRepository.searchByName(names));
    }


    @GetMapping("/statusOfJobSeekerForVacancy")
    public List<String> statusOfJobSeekers() {
        return jobSeekerService.getStatusOfJobSeeker();
    }

    @GetMapping("/candidate/{userId}")
    public List<CandidateResponses> candidateResponses(@PathVariable Long userId) {
        return employerService.getAllCandidates(userId);
    }


    @PostMapping("/candidate/favorite/{userId}")
    public boolean setFavorite(@PathVariable Long userId, @RequestParam(required = false) Long jobSeekerId) {
        return employerService.selectToFavorites(jobSeekerId, userId);
    }


    @GetMapping("/get/jobseeker/{jobSeekerId}")
    public JobSeekerResponses jobSeekerResponses(@PathVariable Long jobSeekerId,
                                                 @RequestHeader (name="Authorization") String token) {
        if (blockedUserService.getUsernameFromToken(token).getBlockedUser().getViewingCandidateData()) {
            throw new BlockedException("THIS USER IS NOT ALLOWED TO VIEW THE CANDIDATE'S DATA");
        }
        return jobSeekerService.getById(jobSeekerId);
    }

    @PostMapping("/create")
    public JobSeekerResponse save(@RequestBody JobSeekerRequest jobSeeker) {
        return jobSeekerService.save(jobSeeker);
    }


    @PostMapping("/update/jobseeker/{id}")
    public JobSeekerResponses update(@PathVariable("id") Long id, @RequestBody JobSeekerRequests jobSeeker) {
        return jobSeekerService.update(id, jobSeeker);
    }


    @GetMapping("/vacancies")
    public List<Vacancy> getVacancies() {
        return vacancyService.getAll();
    }


    @PutMapping("/responded/{vacancyId}/{userId}")
    public VacancyResponse respondedForVacancy(@PathVariable Long vacancyId, @PathVariable Long userId) {
        return vacancyService.responded(vacancyId, userId);
    }

    @GetMapping("/notifications/{userId}")
    public List<NotificationResponse> findAllNotificationsByUserId(@PathVariable Long userId) {
        return jobSeekerService.findAllNotificationsByUserId(userId);
    }

}
