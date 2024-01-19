package com.example.hr_system.service;

import com.example.hr_system.dto.JobSeekerVacanciesResponses;
import com.example.hr_system.dto.image.Response;
import com.example.hr_system.dto.jobSeeker.RespondedResponse;
import com.example.hr_system.dto.vacancy.*;
import com.example.hr_system.entities.JobSeeker;
import com.example.hr_system.entities.Vacancy;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VacancyService {
    VacancyResponse saveVacancy(Long id, VacancyRequest vacancyRequest);


    boolean delete(Long id);

    List<Vacancy> getAll();

    String getIdFromSecurity();

    VacancyResponse update(Long id, VacancyRequest vacancyRequest);

    List<JobSeekerVacanciesResponses> jobSeekerVacancies();
//    List<JobSeekerVacanciesResponses> findByVacancyJobSeekerVacancies(Long id);

    List<VacancyResponse> getMyVacancies(Long id);


//
//    List<Vacancy> employeeVacancies();

    List<JobSeekerVacanciesResponses> searchVacancy(String search);

    List<VacancyResponseForPopularCategories> getPopularPosition();

    List<JobSeekerVacanciesResponses> filter(String category, String position, String country, String city, String experience,
                                             String typeOfEmployments, Boolean salary, Boolean date);

    VacancyResponse updateById(Long id, VacancyRequest vacancyRequest);

    VacancyResponse updateEmployerVacancyByIds(Long employerId, Long vacancyId, VacancyRequest vacancyRequest);

    Response uploadImage(MultipartFile file, Long id) throws IOException;

    VacancyResponse responded(Long vacancyId, Long userId);

    void setStatusOfJobSeeker(Long vacancyId, Long jobSeekerId, String status);

    void setStatusOfVacancy(Long id, String statusOfVacancy);

    List<RespondedResponse> listForResponded(Long vacancyId);

    List<RespondedResponse> listForResponded(Long vacancyId, List<JobSeeker> jobSeekers);

    List<RespondedResponse> listForResponded(Long vacancyId
            , String statusOfJobSeeker, String experience, String applicationDate);

    AboutVacancy aboutVacancy(Long vacancyId);

    List<VacancyResponse> sortedTwoVacancies(Long vacancyId);

    List<Vacancy> respondedJobseekersSearch(String search);

    List<VacancyResponse> employerVacanciesSearchUserId(Long userId, String search);

    List<VacancyResponsesForAdmin> getAllVacancy(String vacancyName, String filterType);

    List<VacancyResponseForPopularCategories> getVacancyCountByCategory();
}

