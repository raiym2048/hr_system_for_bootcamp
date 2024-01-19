package com.example.hr_system.controller;


import com.example.hr_system.dto.GenericResponseForUserResponses;
import com.example.hr_system.dto.admin.*;
import com.example.hr_system.dto.jobSeeker.JobSeekerResponses;
import com.example.hr_system.dto.vacancy.VacancyResponsesForAdmin;
import com.example.hr_system.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminInAController {

    private final EmployerService employerService;
    private final JobSeekerService jobSeekerService;
    private final AdminService adminService;
    private final UserService userService;
    private final BlockedUserService blockedUserService;
    private final VacancyService vacancyService;


    @DeleteMapping("/delete/account")
    @Transactional
    public boolean deleteAccount(@RequestParam String email) {
        adminService.deleteByAccount(email);
        return true;
    }

    @PutMapping("/password/change")
    public String changePassword(@RequestParam String email,
                                 @RequestParam String oldPassword,
                                 @RequestParam(required = false) String newPassword) {
        return userService.changePassword(email, oldPassword, newPassword);
    }

    @DeleteMapping("/delete/employer/{userId}")
    public Boolean deleteEmployerById(@PathVariable Long userId) {
        return employerService.delete(userId);
    }

    @GetMapping("/get/jobseeker/{id}")
    public JobSeekerResponses getById(@PathVariable Long id) {
        return jobSeekerService.getById(id);
    }

    @DeleteMapping("/delete/jobseeker")
    public void delete(@RequestParam(required = false) String email,
                       @RequestParam(required = false) String password

    ) {

        adminService.deleteByAccount(email, password);
    }

    @GetMapping("/response/{vacancyId}")
    public void responseForVacancy(@PathVariable Long vacancyId) {
        jobSeekerService.responseForVacancy(vacancyId);
    }
//

    @GetMapping("/get/all/usersBySearch")
    public List<ResponsesForAdmin> getAllUsers(@RequestParam(required = false) String name,
                                               @RequestParam(required = false) String role) {
        return adminService.getAllUsers(name, role);
    }

    @GetMapping("/get/userById/{userId}")
    public ResponseEntity<GenericResponseForUserResponses> getResponsesById(@PathVariable Long userId) {
        GenericResponseForUserResponses responses = adminService.getResponsesForUserById(userId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/setRoleForUser/{userId}")
    public ResponseEntity<GenericResponseForUserResponses> newStatus(@PathVariable Long userId, @RequestParam(required = false) String role) {
        GenericResponseForUserResponses responses = adminService.setRoleForUser(userId, role);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/processSelectedUsers")
    @Transactional
    public boolean processSelectedUsers(@RequestParam List<Long> selectedUserIds) {
        return adminService.listForDeletingUsers(selectedUserIds);
    }

    @DeleteMapping("/processSelectedVacancy")
    @Transactional
    public boolean processSelectedVacancy(@RequestParam List<Long> selectedVacancyIds) {
        return adminService.listForDeletingVacancy(selectedVacancyIds);
    }

    @PutMapping("/setBlockedForUser/{userId}")
    public BlockedUserResponses setBlockedJobSeeker(@PathVariable Long userId, @RequestBody BlockedUserRequest request) {
        return blockedUserService.isBlockedUser(userId, request);
    }

    @PutMapping("/setBlockedUserByUserID/{userId}")
    public Boolean setBLockedUser(@PathVariable Long userId,
                                  @RequestParam Boolean aBoolean) {
        return adminService.userBlocked(userId, aBoolean);
    }

    @GetMapping("/get/all/vacancies/BySearch")
    public List<VacancyResponsesForAdmin> getAllVacancy(@RequestParam(required = false) String vacancyName,
                                                        @RequestParam(required = false) String date) {
        return vacancyService.getAllVacancy(vacancyName, date);
    }

    @GetMapping("/get/list/support")
    public List<ResponsesForSupport> getListForSupport() {
        return adminService.getListSupport();
    }

    @PostMapping("/create/support")
    public ResponsesForSupport createSupport(@RequestBody SupportRequest request) {
        return adminService.createSupport(request);
    }

    @DeleteMapping("/delete/support/{supportId}")
    public Boolean deleteSupportById(@PathVariable Long supportId) {
        return adminService.deleteSupportById(supportId);
    }
}