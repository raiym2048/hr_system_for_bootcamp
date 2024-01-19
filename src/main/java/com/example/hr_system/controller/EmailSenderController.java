package com.example.hr_system.controller;

import com.example.hr_system.entities.Employer;
import com.example.hr_system.entities.FileData;
import com.example.hr_system.entities.JobSeeker;
import com.example.hr_system.exception.NotFoundException;
import com.example.hr_system.repository.EmployerRepository;
import com.example.hr_system.repository.FileRepository;
import com.example.hr_system.repository.JobSeekerRepository;
import com.example.hr_system.repository.UserRepository;
import com.example.hr_system.service.emailSender.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/employer")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class EmailSenderController {
    private final EmailSenderService emailSenderService;

    @GetMapping("/invite/{jobSeekerId}/{employerId}")
    public void inviteJobSeeker(@PathVariable Long jobSeekerId,
                                @PathVariable Long employerId, @RequestParam String message) {
        emailSenderService.inviteJobSeeker(jobSeekerId, employerId, message);
    }

    @GetMapping("/invite/{jobSeekerId}/{employerId}/{fileId}")
    public void inviteJobSeekerWithFile(@PathVariable Long jobSeekerId,
                                        @PathVariable Long employerId,
                                        @PathVariable Long fileId,
                                        @RequestParam String message) throws MessagingException {
        emailSenderService.inviteJobSeeker(employerId, jobSeekerId, message, fileId);
    }


    @GetMapping("/sendToEmail/{employerId}/{jobSeekerId}/{fileId}")
    public String send(@PathVariable Long employerId, @PathVariable Long jobSeekerId,
                       @PathVariable Long fileId,
                       @RequestParam String message) throws MessagingException {
        emailSenderService.inviteJobSeeker(employerId, jobSeekerId, message, fileId);
        return "ok";
    }
}
