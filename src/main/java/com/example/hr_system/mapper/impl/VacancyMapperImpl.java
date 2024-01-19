package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.contactInformation.ContactInformationResponse;
import com.example.hr_system.dto.salary.SalaryRequest;
import com.example.hr_system.dto.vacancy.*;
import com.example.hr_system.entities.*;
import com.example.hr_system.enums.TypeOfEmployment;
import com.example.hr_system.mapper.*;
import com.example.hr_system.repository.JobSeekerVacancyInformationRepository;
import com.example.hr_system.repository.UserRepository;
import com.example.hr_system.service.EmployerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class VacancyMapperImpl implements VacancyMapper {
    private final SalaryMapper salaryMapper;
    private final ContactInformationMapper contactInformationMapper;
    private final EmployerService employerService;
    private final FileMapper fileMapper;
    private final UserRepository userRepository;
    private final JobSeekerVacancyInformationRepository jobSeekerVacancyInformationRepository;
//      private TypeOfEmployment typeOfEmployment;
//
//    private Double salary;
//
//    private Valute valute;

    @Override
    public VacancyResponse toDto(Vacancy vacancy) {
        VacancyResponse vacancyResponse = new VacancyResponse();
        ContactInformationResponse contactInformation =
                contactInformationMapper.toDto(vacancy.getContactInformation());
        //vacancyResponse.setExperience(vacancy.getE);
        vacancyResponse.setCreationDate(vacancy.getDays()!=null?vacancy.getDays():null);
        vacancyResponse.setId(vacancy.getId());
        if (vacancy.getEmployer()!=null){
            vacancyResponse.setImage(vacancy.getEmployer().getResume()!=null? fileMapper.toDto(vacancy.getEmployer().getResume()):null);

        }
        vacancyResponse.setCompanyName(vacancy.getEmployer().getCompanyName());
        vacancyResponse.setCity(contactInformation.getCity());
        vacancyResponse.setCountry(contactInformation.getCountry());
        vacancyResponse.setCategory(vacancy.getPosition()!=null?vacancy.getPosition().getCategory().getName():null);
        vacancyResponse.setAbout_company(vacancy.getAbout_company());
        vacancyResponse.setPosition(vacancy.getPosition()!=null?
                vacancy.getPosition().getName():
                null);
        vacancyResponse.setIndustry(vacancy.getIndustry());
        vacancyResponse.setDescription(vacancy.getDescription());
        vacancyResponse.setSkills(vacancy.getSkills());
        vacancyResponse.setSalaryResponse(salaryMapper.toDto(vacancy.getSalary()));
        vacancyResponse.setTypeOfEmploymentS(vacancy.getTypeOfEmploymentS());
        vacancyResponse.setRequiredExperience(vacancy.getExperience());
        vacancyResponse.setContactInformationResponse(contactInformationMapper.toDto(vacancy.getContactInformation()));
        vacancyResponse.setAdditionalInformation(vacancy.getAdditionalInformation());
        //vacancyResponse.setCreationDate(employerService.getTimeLeft(String.valueOf(vacancy.getCreationDate())));
        vacancyResponse.setStatusOfVacancy(String.valueOf(vacancy.getStatusOfVacancy().getRussianTranslation()));
        vacancyResponse.setRespondedCount(vacancy.getResponse()!=null?
                vacancy.getResponse():0);
        vacancyResponse.setSearchCounter(vacancy.getSearchCounter()!=null?
                vacancy.getSearchCounter():0);
        return vacancyResponse;
    }

    @Override
    public List<VacancyResponse> toDtos(List<Vacancy> vacancies) {
        List<VacancyResponse> vacancyResponses = new ArrayList<>();
        for (Vacancy vacancy : vacancies) {
            vacancyResponses.add(toDto(vacancy));
        }

        return vacancyResponses;
    }

    @Override
    public VacancyResponse requestToResponse(VacancyRequest vacancyRequest) {
        VacancyResponse vacancyResponse = new VacancyResponse();
        vacancyResponse.setAbout_company(vacancyRequest.getAbout_company());
        vacancyResponse.setPosition(vacancyRequest.getPosition());
        vacancyResponse.setIndustry(vacancyRequest.getIndustry());
        vacancyResponse.setDescription(vacancyRequest.getDescription());
        vacancyResponse.setSkills(vacancyRequest.getSkills());
        vacancyResponse.setSalaryResponse(salaryMapper.toDto(salaryMapper.toEntity(vacancyRequest.getSalaryResponse())));
        vacancyResponse.setTypeOfEmploymentS(TypeOfEmployment.valueOf(vacancyRequest.getTypeOfEmploymentS()));
        vacancyResponse.setRequiredExperience(vacancyRequest.getExperience());
        vacancyResponse.setContactInformationResponse(contactInformationMapper.requestToresponse(vacancyRequest.getContactInformationResponse()));
        vacancyResponse.setAdditionalInformation(vacancyRequest.getAdditionalInformation());
        vacancyResponse.setCreationDate(String.valueOf(LocalDateTime.now()));
//        vacancyResponse.setPositionResponse(vacancyRequest.getPositionRequest());
//        vacancyResponse.setSalaryId(vacancyResponse.getSalaryId());
//        vacancyResponse.setSkills(vacancyRequest.getSkills());
//        vacancyResponse.setContactInformationResponse(contactInformationMapper.requestToresponse(vacancyRequest.getContactInformationRequest()));
//        vacancyResponse.setName(vacancyRequest.getName());
//        vacancyResponse.setDescription(vacancyRequest.getDescription());
        return vacancyResponse;
    }

    @Override
    public Salary toEntity(SalaryRequest salaryRequest) {
        Salary salary = new Salary();
        salary.setSalarySum(salaryRequest.getSalarySum());
        salary.setValute(salaryRequest.getValute());
        salary.setSalaryType(salaryRequest.getSalaryType());
        //salary.setVacancy(employerRepository.findById(employerId));
        return salary;
    }

    @Override
    public RespondedVacancyResponse toRespondedVacancyResponse(JobSeeker jobSeeker,Vacancy vacancy) {
        RespondedVacancyResponse respondedVacancyResponse = new RespondedVacancyResponse();
       // if (jobSeekerVacancyInformationRepository.findAllByJobSeekerId)
        if (jobSeeker==null)
            return null;
        if (!jobSeeker.getVacancies().contains(vacancy))
            return null;
        respondedVacancyResponse.setVacancyId(vacancy.getId());
        respondedVacancyResponse.setImage(vacancy.getResume()!=null? fileMapper.toDto(vacancy.getResume()):null);
        respondedVacancyResponse.setPosition(vacancy.getPosition()!=null?
                vacancy.getPosition().getName():null);
        respondedVacancyResponse.setCategory(vacancy.getPosition()!=null?vacancy.getPosition().getCategory()!=null?
                vacancy.getPosition().getCategory().getName():null:null);
        respondedVacancyResponse.setCompanyName(vacancy.getEmployer()!=null? vacancy.getEmployer().getCompanyName(): null);


        JobSeekerVacancyInformation jobSeekerVacancyInformation =
        jobSeekerVacancyInformationRepository.findByJobSeekerIdAndVacancyId(jobSeeker.getId(), vacancy.getId());
        if (jobSeekerVacancyInformation != null){
             respondedVacancyResponse.setStatusOfJobSeeker(jobSeekerVacancyInformation.getStatusOfJobSeeker());
            respondedVacancyResponse.setApplicationDate(employerService.getTimeLeft(String.valueOf(
                    jobSeekerVacancyInformation.getLocalDateTime()
            )));
        }


        return respondedVacancyResponse;
    }

//    @Override
//    public List<RespondedVacancyResponse> toListRespondedVacancyResponse(JobSeeker jobSeeker, List<Vacancy> vacancies) {
//        return null;
//    }

    @Override
    public List<RespondedVacancyResponse> toListRespondedVacancyResponse(JobSeeker jobSeeker, List<Vacancy> vacancies) {


        List<RespondedVacancyResponse> respondedVacancyResponses = new ArrayList<>();
        for (Vacancy vacancy: vacancies){
            RespondedVacancyResponse respondedVacancyResponse= toRespondedVacancyResponse(jobSeeker, vacancy);
            respondedVacancyResponses.add(respondedVacancyResponse);
        }

        return respondedVacancyResponses;
    }


    @Override
    public AboutVacancy toAboutVacancyResponse(Vacancy vacancy) {
        AboutVacancy aboutVacancy = new AboutVacancy();
        Employer employer = vacancy.getEmployer();
        aboutVacancy.setCreationDate(String.valueOf(vacancy.getCreationDate()));
        aboutVacancy.setPosition(vacancy.getPosition().getName());
        aboutVacancy.setId(vacancy.getId());
        aboutVacancy.setImage(fileMapper.toDto(employer.getResume()));
        aboutVacancy.setCompanyName(employer.getCompanyName());
        aboutVacancy.setAbout_company(vacancy.getAbout_company());
        aboutVacancy.setDescription(vacancy.getDescription());
        aboutVacancy.setSkills(vacancy.getSkills());
        aboutVacancy.setStreet(vacancy.getContactInformation()!=null?
                vacancy.getContactInformation().getStreet_house():null);
        aboutVacancy.setPhoneNumber(employer.getPhoneNumber()!=null?employer.getPhoneNumber():null);
        aboutVacancy.setAdditionalInformation(vacancy.getAdditionalInformation());
        aboutVacancy.setRespondedCount(vacancy.getResponse());
        aboutVacancy.setContactInformationResponse(vacancy.getContactInformation()!=null? contactInformationMapper.toDto(vacancy.getContactInformation()):null);
        aboutVacancy.setCategory(vacancy.getPosition()!=null?vacancy.getPosition().getCategory().getName():null);
        aboutVacancy.setTypeOfEmploymentS(vacancy.getTypeOfEmploymentS());
        aboutVacancy.setRequiredExperience(vacancy.getExperience());
        aboutVacancy.setSalaryResponse(salaryMapper.toDto(vacancy.getSalary()));
        aboutVacancy.setIndustry(vacancy.getIndustry());
        aboutVacancy.setStatusOfVacancy(String.valueOf(vacancy.getStatusOfVacancy()));

        return aboutVacancy;
    }

    @Override
    public VacancyResponsesForAdmin toDtoVacancyForAdmin(Vacancy vacancy) {
        VacancyResponsesForAdmin forAdmin = new VacancyResponsesForAdmin();
        forAdmin.setVacancyId(vacancy.getId());
        forAdmin.setCompanyName(vacancy.getEmployer().getCompanyName());
        //forAdmin.setCompanyName(vacancy.getEmployer().getCompanyName() !=null? vacancy.getEmployer().getCompanyName() : null);
        forAdmin.setPosition(vacancy.getPosition()!=null?
                vacancy.getPosition().getName():null);
        forAdmin.setApplicationDate(employerService.getTimeLeft(String.valueOf(vacancy.getCreationDate())));
        return forAdmin;
    }

    @Override
    public List<VacancyResponsesForAdmin> toDtosVacanciesForAdmin(List<Vacancy> vacancies) {
        List<VacancyResponsesForAdmin> responsesForAdmins = new ArrayList<>();
        for (Vacancy vacancy: vacancies){
            responsesForAdmins.add(toDtoVacancyForAdmin(vacancy));
        }
        return responsesForAdmins;
    }



}
