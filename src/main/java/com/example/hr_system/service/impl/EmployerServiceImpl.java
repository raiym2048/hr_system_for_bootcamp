package com.example.hr_system.service.impl;

import com.example.hr_system.dto.cand.EducationDto;
import com.example.hr_system.dto.employer.EmployerRequest;
import com.example.hr_system.dto.employer.EmployerRequests;
import com.example.hr_system.dto.employer.EmployerResponse;
import com.example.hr_system.dto.employer.EmployerResponses;
import com.example.hr_system.dto.SimpleResponse;
import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.dto.jobSeeker.CandidateResponses;
import com.example.hr_system.dto.notification.NotificationResponse;
import com.example.hr_system.entities.*;
import com.example.hr_system.enums.*;
import com.example.hr_system.mapper.FileMapper;
import com.example.hr_system.mapper.NotificationMapper;
import com.example.hr_system.repository.EmployerRepository;
import com.example.hr_system.repository.JobSeekerRepository;
import com.example.hr_system.repository.UserRepository;
import com.example.hr_system.service.EmployerService;
import com.example.hr_system.mapper.EmployerMapper;

import com.example.hr_system.service.FileDataService;
import com.example.hr_system.service.ProfessionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.sun.activation.registries.LogSupport.log;

@Service
@AllArgsConstructor
public class EmployerServiceImpl implements EmployerService {


    private final EmployerRepository employerRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final EmployerMapper employerMapper;
    private final UserRepository userRepository;
    private final FileDataService fileDataService;
    private final FileMapper fileMapper;
    private final ProfessionService professionService;
    private final NotificationMapper notificationMapper;


    @Override
    public boolean selectToFavorites(Long jobSeekerId, Long userId) throws NotFoundException {
        Employer employer2 = userRepository.findById(userId).get().getEmployer();
        System.out.println(employer2.toString());
        List<JobSeeker> jobSeekers = new ArrayList<>();
        if (employer2.getFavorites()!=null)
            jobSeekers = employer2.getFavorites();


        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).get();
        // for (JobSeeker jobSeeker: jobSeekers){
        if (!jobSeekers.contains(jobSeeker)) {
            jobSeekers.add(jobSeeker);
            System.out.println("adding to favorites\n\n\n");
        } else if (jobSeekers.contains(jobSeeker)) {
            jobSeekers.remove(jobSeeker);
            System.out.println("!adding to favorites\n\n\n");
        }
        //  }
//            if (jobSeekers.size()==0){
//                jobSeekers.add(jobSeekerRepository.findById())
//            }

        Employer employer = userRepository.findById(userId).orElseThrow().getEmployer();
        employer.setFavorites(jobSeekers);

        employerRepository.save(employer);

        return true;
    }

    @Override
    public List<CandidateResponses> favoriteCandidateResponses(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new com.example.hr_system.exception.NotFoundException("User not found!" + userId, HttpStatus.NOT_FOUND));
        Employer employer = user.getEmployer();
        return candidateToDTOs(employer.getFavorites(), employer.getId());
    }


    @Override
    public List<CandidateResponses> getAllCandidates(Long employerId) {
        User user = userRepository.findById(employerId).orElseThrow(() -> new com.example.hr_system.exception.NotFoundException("not found user",HttpStatus.NOT_FOUND));
        return candidateToDTOs(jobSeekerRepository.findAll(), user.getEmployer().getId());
    }
    @Override

    public List<EmployerResponses> getAll() {

        return employerMapper.toDtos(employerRepository.findAll());
    }



    public CandidateResponses convertEntityToCandidateResponse(JobSeeker jobSeeker){
        if (jobSeeker == null){
            System.out.println("its null\n\n\n");
            return null;
        }
        CandidateResponses candidateResponses = new CandidateResponses();
        if (jobSeeker.getImage()==(null)){
        }
        else {
            candidateResponses.setImageId(jobSeeker.getImage().getId());

        }


        candidateResponses.setCandidateId(jobSeeker.getId());
        candidateResponses.setFirstname(jobSeeker.getFirstname());
        candidateResponses.setLastname(jobSeeker.getLastname());
        List<Profession> professions = jobSeeker.getProfessions();

        if(jobSeeker.getProfessions()!=null){
            if (professions != null && !professions.isEmpty() && professions.get(professions.size()-1).getPosition() != null) {
                candidateResponses.setPosition(professions.get(professions.size()-1).getPosition().getName());
            }

        }
        candidateResponses.setExperience(professionService.getExperience(jobSeeker.getProfessions()));
        candidateResponses.setCity(jobSeeker.getCity());
        candidateResponses.setCountry(jobSeeker.getCountry());

        return candidateResponses;
    }



    public List<CandidateResponses> candidateToDTOs(List<JobSeeker>jobSeekers, Long employerId){
        Employer employer = employerRepository.findById(employerId).orElseThrow(()-> new NotFoundException("njsd"));
        List<CandidateResponses>candidateResponses=new ArrayList<>();
        for (JobSeeker jobSeeker:jobSeekers) {
            CandidateResponses responses = convertEntityToCandidateResponse(jobSeeker);
            if(employer.getFavorites().contains(jobSeeker)) {
                responses.setRed(true);
            }
            candidateResponses.add(responses);
        }

        return candidateResponses;
    }

    @Override
    public EmployerResponse save(EmployerRequest employerRequest) {
        Employer employer = new Employer();
        employer.setCompanyName(employerRequest.getCompanyName());
        employer.setEmail(employerRequest.getEmail());
        employer.setPassword(employerRequest.getPassword());
        employerRepository.save(employer);

        EmployerResponse employerResponse = new EmployerResponse();
        employerResponse.setCompanyName(employerRequest.getCompanyName());
        employerResponse.setId(employer.getId());

        return employerResponse;
    }

    @Override
    public EmployerResponses update(Long userId, EmployerRequests employerRequests) {
        Long id = userRepository.findById(userId).orElseThrow().getEmployer().getId();
        EmployerResponses employerResponses = getById(userId);
        Employer employer;

        employer = convertToEntity(id, employerRequests);
        employerResponses =  employerMapper.toDto(employer);
        employerRepository.save(employer);

        return employerResponses;
    }
    @Override
    public String getTimeLeft(String creationDateVacancy) {

        if (creationDateVacancy == null) {
            return "N/A";
        }

        LocalDateTime creationDate = LocalDateTime.parse(creationDateVacancy);
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(creationDate, now);
        long days = duration.toDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes();

        return String.valueOf(days>0?days +" days":hours>0?hours+" hours":minutes+" minutes");
    }

    @Override
    public String getTimeLeft2(LocalDateTime creationDateVacancy) {

        if (creationDateVacancy == null) {
            return "N/A";
        }

        LocalDateTime creationDate = creationDateVacancy;
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(creationDate, now);
        long days = duration.toDays();
        long minutes = duration.toMinutes();
      //  System.out.println("\n\n\n the days: "+days+"\n the minutes: "+ minutes);
        //System.out.println("days: "+days);
        //System.out.println("minutes: "+minutes);
        if(minutes>=0&&days<1){
            return "Сегодня";
        }
        else if (days>1&&days<8){
            return "На этой неделе";
        }
        else if (days>7&&days<30){
            return "В этом месяце";
        }
        else if (days>31&&days<361){
            return "В этом году";
        }
        else {
            return "Старое";
        }
    }

    @Override
    public EmployerResponses getById(Long id) {
        System.out.println("the id from front: " + id);

        User user = userRepository.findById(id).orElseThrow(() ->
                new com.example.hr_system.exception.NotFoundException("User not found!" + id,HttpStatus.NOT_FOUND));
        Long employerId = user.getEmployer().getId();
        return employerMapper.toDto(employerRepository.findById(employerId).orElseThrow(() -> new NotFoundException("we don't have employer with id :" + id)));
    }
    @Override
    public SimpleResponse deleteById(Long id) {
        boolean emp = employerRepository.existsById(id);
        if (!emp) {
            throw new RuntimeException("we don't have employer with id :" + id);
        }
        employerRepository.deleteById(id);
        SimpleResponse simpleResponse = new SimpleResponse();
        simpleResponse.setMessage("You deleted employer successfully!!!");
        return simpleResponse;
    }
    @Override
    public Employer convertToEntity(Long id, EmployerRequests employerRequests) {
        if (employerRequests == null) {
            return null;
        }
        Employer employer = employerRepository.findById(id).get();
        employer.setCompanyName(employerRequests.getCompanyName());
        employer.setAboutCompany(employerRequests.getAboutCompany());
        employer.setCountry(employerRequests.getCountry());
        employer.setCity(employerRequests.getCity());
        employer.setAddress(employerRequests.getAddress());
        employer.setEmail(employerRequests.getEmail());
        employer.setPhoneNumber(employerRequests.getPhoneNumber());
        return employer;
    }

    @Override
    public List<NotificationResponse> findAllNotificationsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new com.example.hr_system.exception.NotFoundException("USER not found!",HttpStatus.NOT_FOUND));
        return notificationMapper.toDtos(user.getNotification());
    }

    @Override
    public List<CandidateResponses> filter(String position, String education, String country, String city, String experience) {

        return null;
    }

    @Override
    public FileResponse uploadResume(MultipartFile file, Long id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("user not found!" + id));
        if (user.getRole()==Role.EMPLOYER){
            Employer employer = user.getEmployer();
            if (employer.getResume() != null) {
                FileData fileData = new FileData();
                fileData = employer.getResume();
                employer.setResume(null);
                FileData save = fileDataService.uploadFile(file, fileData);
                employer.setResume(save);
                employerRepository.save(employer);
                return fileMapper.toDto(save);
            } else {
                FileData fileData=fileDataService.uploadFile(file);
                employer.setResume(fileData);
                employerRepository.save(employer);
                return fileMapper.toDto(fileData);
            }
        }

        JobSeeker employer = user.getJobSeeker();

        if (employer.getResume() != null) {
            FileData fileData = new FileData();
            fileData = employer.getResume();
            employer.setResume(null);
            FileData save = fileDataService.uploadFile(file, fileData);
            employer.setResume(save);
            jobSeekerRepository.save(employer);
            return fileMapper.toDto(save);
        } else {
            FileData fileData=fileDataService.uploadFile(file);
            employer.setResume(fileData);
            jobSeekerRepository.save(employer);
            return fileMapper.toDto(fileData);
        }
    }


    @Override
    public FileResponse uploadFileChat(MultipartFile file, Long id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("user not found!" + id));
        FileData fileData = new FileData();
        FileData save = fileDataService.uploadFile(file, fileData);
        return fileMapper.toDto(save);

    }


    @Override
    public boolean containsEducation(String str) {
        for (Education education : Education.values()) {
            if (education.name().equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean containsTypeOfEmployment(String str) {
        for (TypeOfEmployment education : TypeOfEmployment.values()) {
            if (education.name().equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsStatusOfJobSeeker(String str) {
        if (str==null)
            return false;

        for (StatusOfJobSeeker education : StatusOfJobSeeker.values()) {
            if (education.name().equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsApplicationDate(String str) {
        for (ApplicationDate education : ApplicationDate.values()) {
            if (education.name().equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean delete(Long userId) {
        User user = userRepository.findById(userId).get();
        userRepository.delete(user);
        return true;
    }

    @Override
    public List<EducationDto> getEducations() {

        List<EducationDto> strings = new ArrayList<>();
        for (Education education : Education.values()) {
            EducationDto dto = new EducationDto();
            dto.setName(education.name());
            strings.add(dto);
        }
        return strings;
    }

    @Override
    public List<String> getTypeOfEmployments() {

        List<String> strings = new ArrayList<>();
        strings.add(String.valueOf(TypeOfEmployment.неполный_рабочий_день));
        strings.add(String.valueOf(TypeOfEmployment.полный_рабочий_день));
        strings.add(String.valueOf(TypeOfEmployment.удаленная_работа));
        return strings;
    }

    @Override
    public List<String> getSalaryTypes() {

        List<String> strings = new ArrayList<>();
        strings.add(String.valueOf(SalaryType.гибридный));
        strings.add(String.valueOf(SalaryType.полный));
        strings.add(String.valueOf(SalaryType.фиксированный));
        return strings;
    }

    @Override
    public List<String> getValutes() {

        List<String> list = new ArrayList<>();
        list.add(String.valueOf(Valute.доллар));
        list.add(String.valueOf(Valute.евро));
        list.add(String.valueOf(Valute.тенге));
        list.add(String.valueOf(Valute.сом));
        list.add(String.valueOf(Valute.рубль));
        list.add(String.valueOf(Valute.сум));
        return list;
    }

    @Override
    public FileResponse uploadImage(MultipartFile file, Long id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("user not found!" + id));
        if (user.getRole()==Role.EMPLOYER){
            Employer employer = user.getEmployer();
            if (employer.getResume() != null) {
                FileData fileData = new FileData();
                fileData = employer.getResume();
                employer.setResume(null);
                FileData save = fileDataService.uploadFile(file, fileData);
                employer.setResume(save);
                employerRepository.save(employer);
                return fileMapper.toDto(save);
            } else {
                FileData fileData=fileDataService.uploadFile(file);
                employer.setResume(fileData);
                employerRepository.save(employer);
                return fileMapper.toDto(fileData);
            }
        }

        JobSeeker jobSeeker = user.getJobSeeker();

        if (jobSeeker.getResume() != null) {
            FileData fileData = new FileData();
            fileData = jobSeeker.getResume();
            jobSeeker.setImage(null);
            FileData save = fileDataService.uploadFile(file, fileData);
            jobSeeker.setImage(save);
            jobSeekerRepository.save(jobSeeker);
            return fileMapper.toDto(save);
        } else {
            FileData fileData=fileDataService.uploadFile(file);
            jobSeeker.setImage(fileData);
            jobSeekerRepository.save(jobSeeker);
            return fileMapper.toDto(fileData);
        }
    }



}
