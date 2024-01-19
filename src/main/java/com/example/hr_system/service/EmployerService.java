package com.example.hr_system.service;

import com.example.hr_system.dto.SimpleResponse;
import com.example.hr_system.dto.cand.EducationDto;
import com.example.hr_system.dto.employer.EmployerRequest;
import com.example.hr_system.dto.employer.EmployerRequests;
import com.example.hr_system.dto.employer.EmployerResponse;
import com.example.hr_system.dto.employer.EmployerResponses;
import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.dto.jobSeeker.CandidateResponses;
import com.example.hr_system.dto.notification.NotificationResponse;
import com.example.hr_system.entities.Employer;
import com.example.hr_system.entities.Vacancy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface EmployerService {
    boolean selectToFavorites(Long jobSeekerId, Long employerId);

    List<CandidateResponses> favoriteCandidateResponses(Long userId);

    List<CandidateResponses> getAllCandidates(Long employerId);

    List<EmployerResponses> getAll();



    EmployerResponse save(EmployerRequest employerRequest);

    EmployerResponses update(Long id, EmployerRequests employerRequests);

    String getTimeLeft(String creationDateVacancy);

    String getTimeLeft2(LocalDateTime creationDateVacancy);

    EmployerResponses getById(Long id);

    SimpleResponse deleteById(Long id);

    Employer convertToEntity(Long id, EmployerRequests employerRequests);

    List<NotificationResponse> findAllNotificationsByUserId(@PathVariable Long userId);






    List<CandidateResponses> filter(String position, String education, String country, String city, String experience);

    FileResponse uploadResume(MultipartFile file, Long id) throws IOException;

    FileResponse uploadFileChat(MultipartFile file, Long id) throws IOException;

    boolean containsEducation(String str);

    boolean containsTypeOfEmployment(String str);

    boolean containsStatusOfJobSeeker(String str);

    boolean containsApplicationDate(String str);

    Boolean delete(Long userId);

    List<EducationDto> getEducations();

    List<String> getTypeOfEmployments();

    List<String> getSalaryTypes();

    List<String> getValutes();

    FileResponse uploadImage(MultipartFile file, Long employerId)throws IOException;


    // FileResponse uploadResume(MultipartFile file, Long id) throws IOException;

    // FileResponse uploadResume(MultipartFile file, Long id);
}
