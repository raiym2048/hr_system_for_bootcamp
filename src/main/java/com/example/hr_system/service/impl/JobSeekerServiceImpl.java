package com.example.hr_system.service.impl;

import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.dto.jobSeeker.*;
import com.example.hr_system.dto.notification.NotificationResponse;
import com.example.hr_system.entities.*;
import com.example.hr_system.enums.Education;
import com.example.hr_system.enums.Role;
import com.example.hr_system.enums.StatusOfJobSeeker;
import com.example.hr_system.exception.NotFoundException;
import com.example.hr_system.mapper.*;
import com.example.hr_system.repository.*;
import com.example.hr_system.service.EmployerService;
import com.example.hr_system.service.FileDataService;

import com.example.hr_system.service.JobSeekerService;
import com.example.hr_system.service.ProfessionService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class JobSeekerServiceImpl implements JobSeekerService {
    private final JobSeekerMapper jobSeekerMapper;
    private final JobSeekerRepository jobSeekerRepository;
    private final VacancyRepository vacancyRepository;
    private final PositionRepository positionRepository;
    private final FileDataService fileDataService;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final NotificationMapper notificationMapper;
    private final EmployerService employerService;
    private final ExperienceRepository experienceRepository;
    private final EducationMapper educationMapper;
    private final ProfessionMapper professionMapper;
    private final ProfessionService professionService;
    private final EducationRepository educationRepository;
    private final ProfessionRepository professionRepository;

    @Override
    public List<CandidateResponses> findByName(String name, String email) {
        if (name.isEmpty() || name==null)
            return jobSeekerMapper.listConvertToCandidateResponse(jobSeekerRepository.findAll(),email);
        List<JobSeeker> jobSeekers = jobSeekerRepository.searchByName(name);
        System.out.println("the size"+jobSeekers.size());
        return jobSeekerMapper.listConvertToCandidateResponse(jobSeekers, email);
    }
    @Override
    public List<String> getStatusOfJobSeeker() {

        List<String> strings = new ArrayList<>();
        strings.add(String.valueOf(StatusOfJobSeeker.отклонено));
        strings.add(String.valueOf(StatusOfJobSeeker.принято));
        strings.add(String.valueOf(StatusOfJobSeeker.предложение));
        strings.add(String.valueOf(StatusOfJobSeeker.рассматривается));
        strings.add(String.valueOf(StatusOfJobSeeker.рассматривается));
        return strings;
    }

    @Override
    public FileResponse uploadResume(MultipartFile file, Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("user not found!" + id,HttpStatus.NOT_FOUND));
        JobSeeker jobSeeker = user.getJobSeeker();

        if (jobSeeker.getResume() != null) {
            FileData fileData = jobSeeker.getResume();
            jobSeeker.setResume(null);
            FileData save = fileDataService.uploadFile(file, fileData);
            jobSeeker.setResume(save);
            jobSeekerRepository.save(jobSeeker);
            return fileMapper.toDto(save);
        } else {
            FileData fileData = fileDataService.uploadFile(file);
            jobSeeker.setResume(fileData);
            jobSeekerRepository.save(jobSeeker);
            return fileMapper.toDto(fileData);
        }
    }


    @Override
    public List<JobSeeker> getAll() {
        return jobSeekerRepository.findAll();
    }

    @Override
    public List<JobSeekerResponses> getAllJobSeekers() {

        return jobSeekerMapper.toDtos(jobSeekerRepository.findAll());

    }


    @Override
    public JobSeekerResponse save(JobSeekerRequest jobSeeker) {
        JobSeeker jobSeeker1 = new JobSeeker();
        jobSeeker1.setFirstname(jobSeeker.getFirstname());
        jobSeeker1.setLastname(jobSeeker.getLastname());

        jobSeeker1.setEmail(jobSeeker.getEmail());
        jobSeeker1.setPassword(jobSeeker.getPassword());
        jobSeeker1.setRole(Role.JOB_SEEKER);
        //jobSeekerRepository.save(jobSeeker1);

        return new JobSeekerResponse(jobSeeker1.getId(), jobSeeker1.getFirstname(), jobSeeker1.getLastname(), jobSeeker1.getRole());
    }


    @Override
    public JobSeekerResponses update(Long id, JobSeekerRequests jobSeeker) {
        if (jobSeeker.getImageId() == null) {
            new NotFoundException("we could not found image",HttpStatus.NOT_FOUND);

        }
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user can be null"));
        JobSeeker jobSeeker1 = user.getJobSeeker();
        jobSeeker1.setResume(
                jobSeeker.getResumeId() == null ? null :
                        fileRepository.findById(jobSeeker.getResumeId()).get());
        jobSeeker1.setImage(
                jobSeeker.getImageId() == null ? null :
                        fileRepository.findById(jobSeeker.getImageId()).get());
        jobSeeker1.setFirstname(jobSeeker.getFirstname());
        jobSeeker1.setLastname(jobSeeker.getLastname());
        jobSeeker1.setBirthday(jobSeeker.getBirthday());
        jobSeeker1.setCountry(jobSeeker.getCountry());
        jobSeeker1.setCity(jobSeeker.getCity());
        jobSeeker1.setAddress(jobSeeker.getAddress());
        jobSeeker1.setPhoneNumber(jobSeeker.getPhoneNumber());
        jobSeeker1.setAbout(jobSeeker.getAbout());
        jobSeeker1.setEducationJobs(null);
        jobSeeker1.setProfessions(null);
        educationRepository.deleteAll(educationRepository.findAllByJobSeekerId(jobSeeker1.getId()));
        professionRepository.deleteAll(professionRepository.findAllByJobSeekerId(jobSeeker1.getId()));
        jobSeeker1.setEducationJobs(educationMapper.RequestToDtoS(jobSeeker.getEducationRequests(), jobSeeker1));
        jobSeeker1.setProfessions(professionMapper.RequestToDtoS(jobSeeker.getProfessionRequests(), jobSeeker1));
        String experience = professionService.getExperience(
                professionMapper.requestToEntitys(
                        jobSeeker.getProfessionRequests()));
        System.out.println("the experience1221"+experience);
        jobSeeker1.setExperienceJ(experienceRepository.findByName(
                experience).get());
        jobSeeker1.setRole(Role.JOB_SEEKER);

        jobSeekerRepository.save(jobSeeker1);
        return jobSeekerMapper.toDto(jobSeeker1);

    }

    @Override
    public SimpleMessage delete(Long id) {
        jobSeekerRepository.deleteById(id);
        return new SimpleMessage("You have deleted" + id);
    }

    @Override
    public JobSeekerResponses getById(Long id) {
        return jobSeekerMapper.toDto(jobSeekerRepository.findById(id).orElseThrow(() -> new NotFoundException("not found jobseeker with id: " + id, HttpStatus.NOT_FOUND)));
    }

    @Override
    public void responseForVacancy(Long vacancyId) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElseThrow(() -> new NotFoundException("Vacancy not found!",HttpStatus.NOT_FOUND));
        if (vacancy.getIsResponse().equals(false)) {
            vacancy.setResponse(vacancy.getJobSeekers().size() + 1);
        }
        vacancy.setIsResponse(true);
        vacancyRepository.save(vacancy);
    }
    @Override
    public List<CandidateResponses> filter2(String position, String education, String country, String city, String experience) {
        List<JobSeeker> jobSeekers = filterJobSeekers(positionRepository.findByName(position), employerService.containsEducation(education) ? Education.valueOf(education) : null, country, city, experience);
        System.out.println("\n\n the size:"+jobSeekers.size());
        return jobSeekerMapper.listConvertToCandidateResponse(jobSeekers,"");
    }




    @Override
    public List<JobSeeker> filterJobSeekers(
            Position position,
            Education education,
            String country,
            String city,
            String experienceName
    ) {
        System.out.println("\n\n\n the experience: "+ experienceName);
        System.out.println(experienceRepository.findByName(experienceName).isPresent()?"\n\nfound!"+experienceName:"\n\nnot found!");
        System.out.println((position != null ? position.getName() : null)+
        education!=null?education:null+country+city+experienceName);
        if (position == null && (country == null || country.isEmpty()) &&
                (city == null || city.isEmpty()) && (experienceName == null || experienceName.isEmpty()) &&
                education == null) {
            return jobSeekerRepository.findAll();
        }

        return jobSeekerRepository.filterJobSeekers(
                position != null ? position.getName() : null,
                education,
                country != null && !country.isEmpty() ? country : null,
                city != null && !city.isEmpty() ? city : null,
                experienceName
        );
    }


    @Override
    public List<JobSeeker> searchByFirstAndLastName(String firstname, String lastname) {
        return jobSeekerRepository.searchJobSeekers(
                firstname != null && !firstname.isEmpty() ? firstname : null,
                lastname != null && !lastname.isEmpty() ? lastname : null

        );
    }

    @Override
    public List<NotificationResponse> findAllNotificationsByUserId(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("USER not found!",HttpStatus.NOT_FOUND));
        return notificationMapper.toDtos(user.getNotification());
    }

    @Override
    public JobSeekerResponses getById2(Long userId) {
        Long id = userRepository.findById(userId).orElseThrow().getJobSeeker().getId();
        return jobSeekerMapper.toDto(jobSeekerRepository.findById(id).orElseThrow(() -> new NotFoundException("not found jobseeker with id: " + id,HttpStatus.NOT_FOUND)));

    }

}
