package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.JobSeekerVacanciesResponses;
import com.example.hr_system.dto.education.EducationResponse;
import com.example.hr_system.dto.jobSeeker.CandidateResponses;
import com.example.hr_system.dto.jobSeeker.JobSeekerRequest;
import com.example.hr_system.dto.jobSeeker.JobSeekerResponses;
import com.example.hr_system.dto.jobSeeker.RespondedResponse;
import com.example.hr_system.dto.profession.ProfessionResponse;
import com.example.hr_system.entities.*;
import com.example.hr_system.enums.StatusOfJobSeeker;
import com.example.hr_system.mapper.FileMapper;
import com.example.hr_system.mapper.JobSeekerMapper;
import com.example.hr_system.mapper.ProfessionMapper;
import com.example.hr_system.mapper.VacancyMapper;
import com.example.hr_system.repository.EmployerRepository;
import com.example.hr_system.repository.FileRepository;
import com.example.hr_system.repository.JobSeekerVacancyInformationRepository;
import com.example.hr_system.repository.UserRepository;
import com.example.hr_system.service.EmployerService;
import com.example.hr_system.service.ProfessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JobSeekerMapperImpl implements JobSeekerMapper {
    private final EmployerService employerService;
    private final EducationMapperImpl educationMapper;
    private final ProfessionMapper professionMapper;
    private final ProfessionService professionService;
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    private final UserRepository userRepository;
    private final JobSeekerVacancyInformationRepository jobSeekerVacancyInformationRepository;
    private VacancyMapper vacancyMapper;
    private final EmployerRepository employerRepository;

    @Override
    public JobSeekerResponses toDto(JobSeeker jobSeeker) {
        if (jobSeeker == null) {
            return null;
        }
        JobSeekerResponses response = new JobSeekerResponses();
        response.setId(jobSeeker.getId());

        if (jobSeeker.getResume() != null) {
            response.setResumeResponse(fileMapper.toDto(fileRepository.findById(jobSeeker.getResume().getId()).get()));
        }
        if (jobSeeker.getImage() != null) {
            response.setImageResponse(fileMapper.toDto(fileRepository.findById(jobSeeker.getImage().getId()).get())) ;
        }
        response.setFirstname(jobSeeker.getFirstname());
        response.setLastname(jobSeeker.getLastname());
        response.setAbout(jobSeeker.getAbout());
        if (jobSeeker.getEducationJobs()!=null){

        }

        List<EducationResponse> educationResponses = educationMapper.toDtos(jobSeeker.getEducationJobs(), jobSeeker);
        response.setEducationResponse(educationResponses);
        List<ProfessionResponse> professionResponses = professionMapper.toDtos(jobSeeker.getProfessions(), jobSeeker);
        response.setProfessionResponse(professionResponses);


        response.setBirthday(jobSeeker.getBirthday());
        response.setCountry(jobSeeker.getCountry());
        response.setCity(jobSeeker.getCity());
        response.setAddress(jobSeeker.getAddress());
        response.setEmail(jobSeeker.getEmail());
        response.setPhoneNumber(jobSeeker.getPhoneNumber());
        response.setRole(jobSeeker.getUser().getRole());
        response.setStatusOfJobSeeker(employerService.containsStatusOfJobSeeker(String.valueOf(jobSeeker.getStatusOfJobSeeker()))?
                jobSeeker.getStatusOfJobSeeker(): null);
        if (jobSeeker.getUser().getBlockedUser() != null){
            response.setCommunicationWithEmployers(jobSeeker.getUser().getBlockedUser().getCommunicationWithEmployers());
            response.setViewingAndSearchingForVacancies(jobSeeker.getUser().getBlockedUser().getViewingAndSearchingForVacancies());
            response.setViewingCompanyData(jobSeeker.getUser().getBlockedUser().getViewingCompanyData());
            response.setViewTheStatusOfResponded(jobSeeker.getUser().getBlockedUser().getViewTheStatusOfResponded());
        }
        response.setBlocked(jobSeeker.getUser().getBlocked());
        return response;
    }

    @Override
    public List<JobSeekerResponses> toDtos(List<JobSeeker> jobSeekers) {
        List<JobSeekerResponses> jobSeekerResponses = new ArrayList<>();
        for (JobSeeker jobSeeker : jobSeekers) {
            jobSeekerResponses.add(toDto(jobSeeker));
        }
        return jobSeekerResponses;
    }

    @Override
    public JobSeekerVacanciesResponses convertToVacancyJobSeekerResponse(Vacancy vacancy) {
        if (vacancy == null) {
            return null;
        }
        JobSeekerVacanciesResponses vacanciesResponses = new JobSeekerVacanciesResponses();
        vacanciesResponses.setId(vacancy.getId());
        vacanciesResponses.setOwnerName(vacancy.getAbout_company());
        vacanciesResponses.setVacancyResponse(vacancyMapper.toDto(vacancy));
        return vacanciesResponses;
    }

    @Override
    public List<JobSeekerVacanciesResponses> convertToVacancyJobSeekerResponses(List<Vacancy> vacancies) {
        List<JobSeekerVacanciesResponses> jobSeekerVacanciesResponses = new ArrayList<>();
        for (Vacancy vacancy : vacancies) {
            jobSeekerVacanciesResponses.add(convertToVacancyJobSeekerResponse(vacancy));
        }
        return jobSeekerVacanciesResponses;
    }

    @Override
    public List<CandidateResponses> listConvertToCandidateResponse(List<JobSeeker> jobSeekers, String email) {
        List<CandidateResponses> candidateResponses = new ArrayList<>();
        for(JobSeeker jobSeeker: jobSeekers){
            candidateResponses.add(convertToCandidateResponse(jobSeeker, email));
        }
        return candidateResponses;
    }
    @Override
    public CandidateResponses convertToCandidateResponse(JobSeeker jobSeeker, String email) {
        Optional<User> user = userRepository.findByEmail(email);

            CandidateResponses candidateResponses = new CandidateResponses();
            if (jobSeeker.getImage()==(null)){

            }else {
                candidateResponses.setImageId(jobSeeker.getImage().getId());

            }
            candidateResponses.setCandidateId(jobSeeker.getId());
        if (!user.isEmpty()){
            Employer employer = null;
            employer =  user.get().getEmployer();
            if (employer.getFavorites().contains(jobSeeker)) {
                candidateResponses.setRed(true);
            }
        }
        else {
            candidateResponses.setRed(false);
//some changes
        }

            candidateResponses.setFirstname(jobSeeker.getFirstname());
            candidateResponses.setLastname(jobSeeker.getLastname());
            if (jobSeeker.getProfessions()!=null){
                if (jobSeeker.getProfessions().size()>0){
                    if (jobSeeker.getProfessions().get(jobSeeker.getProfessions().size()-1).getPosition()!=null){
                        candidateResponses.setPosition(jobSeeker.getProfessions().get(jobSeeker.getProfessions().size()-1).getPosition().getName());
                    }
                }

            }
            candidateResponses.setExperience(jobSeeker.getExperienceJ()!=null? jobSeeker.getExperienceJ().getName():null);

            candidateResponses.setCity(jobSeeker.getCity());
            candidateResponses.setCountry(jobSeeker.getCountry());
            return candidateResponses;




    }

    @Override
    public List<RespondedResponse> toDtosForListResponded(List<JobSeeker> jobSeekers, Long vacancyId) {
        List<RespondedResponse> respondedResponses = new ArrayList<>();
        for (JobSeeker jobSeeker: jobSeekers){
            respondedResponses.add(toDtoForResponded(jobSeeker, vacancyId));
        }
        return respondedResponses;
    }

    @Override
    public RespondedResponse toDtoForResponded(JobSeeker jobSeeker, Long vacancyId) {
        if (jobSeeker == null) {
            System.out.println("its null\n\n\n");
            return null;
        }
        RespondedResponse respondedResponses = new RespondedResponse();
        respondedResponses.setVacancyId(vacancyId);
        respondedResponses.setId(jobSeeker.getId());
        respondedResponses.setFirstname(jobSeeker.getFirstname());
        respondedResponses.setLastname(jobSeeker.getLastname());
        if(jobSeeker.getProfessions()!=null){
            if (jobSeeker.getProfessions().size()>0){
                if (jobSeeker.getProfessions().get(jobSeeker.getProfessions().size()-1).getPosition()!=null){
                    respondedResponses.setPosition(jobSeeker.getProfessions().get(jobSeeker.getProfessions().size()-1).getPosition().getName());
                    if (jobSeeker.getProfessions().get(jobSeeker.getProfessions().size()-1).getPosition().getCategory()!=null){
                        respondedResponses.setCategory(
                                jobSeeker.getProfessions().get(jobSeeker.getProfessions().size()-1).getPosition().getCategory().getName());
                    }
                }
            }

        }

        respondedResponses.setExperience(professionService.getExperience(jobSeeker.getProfessions()));
        respondedResponses.setCountry(jobSeeker.getCountry());
        respondedResponses.setCity(jobSeeker.getCity());
        JobSeekerVacancyInformation jobSeekerVacancyInformation =
        jobSeekerVacancyInformationRepository.findByJobSeekerIdAndVacancyId(
                jobSeeker.getId(), vacancyId
        );
        respondedResponses.setLocalDate(employerService.getTimeLeft(String.valueOf(jobSeekerVacancyInformation.getLocalDateTime())));
        respondedResponses.setStatusOfJobSeeker(jobSeekerVacancyInformation.getStatusOfJobSeeker()==
                StatusOfJobSeeker.отправлено?null:
                jobSeekerVacancyInformation.getStatusOfJobSeeker()==null?null: jobSeekerVacancyInformation.getStatusOfJobSeeker());

        return respondedResponses;
    }

    @Override
    public JobSeeker toEntity(JobSeekerRequest jobSeekerRequest) {
        JobSeeker jobSeeker = new JobSeeker();
        jobSeekerRequest.setFirstname(jobSeekerRequest.getFirstname());
        jobSeekerRequest.setLastname(jobSeekerRequest.getLastname());
        jobSeekerRequest.setEmail(jobSeekerRequest.getEmail());
        jobSeekerRequest.setPassword(jobSeekerRequest.getPassword());
        return jobSeeker;
    }
}
