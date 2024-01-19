package com.example.hr_system.service;

import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.dto.jobSeeker.*;
import com.example.hr_system.dto.notification.NotificationResponse;
import com.example.hr_system.entities.Experience;
import com.example.hr_system.entities.JobSeeker;
import com.example.hr_system.entities.Position;
import com.example.hr_system.enums.Education;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobSeekerService {
    List<String> getStatusOfJobSeeker();

    List<CandidateResponses> findByName(String name, String email);


    FileResponse uploadResume(MultipartFile file, Long id);

    List<JobSeeker> getAll();

    List<JobSeekerResponses> getAllJobSeekers();

    JobSeekerResponse save(JobSeekerRequest jobSeeker);

    JobSeekerResponses update(Long id, JobSeekerRequests jobSeeker);

    SimpleMessage delete(Long id);

    JobSeekerResponses getById(Long id);

    void responseForVacancy(Long vacancyId);

    List<CandidateResponses> filter2(String position, String education, String country, String city, String experience);


    List<JobSeeker> filterJobSeekers(
            Position position,
            Education education,
            String country,
            String city,
            String experience
    );

    List<JobSeeker> searchByFirstAndLastName(
            String firstname,
            String lastname
    );

    List<NotificationResponse> findAllNotificationsByUserId(@PathVariable Long userId);

    JobSeekerResponses getById2(Long userId);

    // List<RespondedResponse> filterJobSeekers(StatusOfJobSeeker statusOfJobSeeker, Experience experience, LocalDate applicationDate);
}
