package com.example.hr_system.service.impl;

import com.example.hr_system.config.MyHandler;
import com.example.hr_system.dto.JobSeekerVacanciesResponses;
import com.example.hr_system.dto.image.Response;
import com.example.hr_system.dto.jobSeeker.RespondedResponse;
import com.example.hr_system.dto.message.TypeResponse;
import com.example.hr_system.dto.vacancy.*;
import com.example.hr_system.entities.*;
import com.example.hr_system.enums.*;
import com.example.hr_system.exception.NotFoundException;
import com.example.hr_system.mapper.*;
import com.example.hr_system.repository.*;
import com.example.hr_system.repository.JobSeekerVacancyInformationRepository;
import com.example.hr_system.service.EmployerService;
import com.example.hr_system.service.FileDataService;
import com.example.hr_system.service.VacancyService;

import javax.persistence.EntityNotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();


    private final UserRepository userRepository;
    private final EmployerRepository employeeRepository;
    private final FileDataService fileDataService;
    private final VacancyRepository vacancyRepository;
    private final SalaryRepository salaryRepository;
    private final PositionRepository positionRepository;
    private final SalaryMapper salaryMapper;
    private final JobSeekerRepository jobSeekerRepository;
    private final VacancyMapper vacancyMapper;
    private final JobSeekerMapper jobSeekerMapper;
    private final JobSeekerVacancyInformationRepository jobSeekerVacancyInformationRepository;
    private final NotificationRepository notificationRepository;
    private final ContactInformationServiceImpl contactInformationService;
    private final ContactInformationRepository contactInformationRepository;
    private final ExperienceRepository experienceRepository;
    private final EmployerService employerService;
    private final ContactInformationMapper contactInformationMapper;
    private final JobSeekerVacancyInformationMapper jobSeekerVacancyInformationMapper;
    private final FileRepository fileRepository;
    private final MyHandler myHandler;
    private final CategoryRepository categoryRepository;


    @Override
    public VacancyResponse saveVacancy(Long id, VacancyRequest vacancyRequest) {
        Vacancy vacancy = new Vacancy();

        vacancy.setSalary(vacancyRequest.getSalaryResponse() != null ? salaryMapper.toEntity(vacancyRequest.getSalaryResponse()) : null);
        vacancy.setAbout_company(vacancyRequest.getAbout_company());
        vacancy.setIndustry(vacancyRequest.getIndustry());
        vacancy.setExperience(vacancyRequest.getExperience());
        vacancy.setAdditionalInformation(vacancyRequest.getAdditionalInformation());
        vacancy.setTypeOfEmploymentS(
                employerService.containsTypeOfEmployment(vacancyRequest.getTypeOfEmploymentS()) ?
                        TypeOfEmployment.valueOf(vacancyRequest.getTypeOfEmploymentS()) : TypeOfEmployment.неполный_рабочий_день);
        vacancy.setSkills(vacancyRequest.getSkills());
        vacancy.setDescription(vacancyRequest.getDescription());

        vacancy.setStatusOfVacancy(StatusOfVacancy.open);

        Position position = positionRepository.findByName(vacancyRequest.getPosition());
        vacancy.setPosition(position);
        vacancy.setResponse(0);

        Salary salary = salaryMapper.toEntity(vacancyRequest.getSalaryResponse());
        salaryRepository.save(salary);
        vacancy.setSalary(salary);
        vacancy.setCreationDate(LocalDateTime.now());
        vacancy.setDays(employerService.getTimeLeft2(LocalDateTime.now()));


        ContactInformation contactInformation = contactInformationService.convertToEntity(vacancyRequest.getContactInformationResponse());
        contactInformationRepository.save(contactInformation);
        vacancy.setContactInformation(contactInformation);
        vacancy.setSearchCounter(vacancy.getSearchCounter() == null ? 0 : vacancy.getSearchCounter());
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Employee not found", HttpStatus.NOT_FOUND));

        Employer employer = user.getEmployer();
        vacancy.setEmployer(employer);
        List<Notification> notifications = new ArrayList<>();


        for (User userJobSeeker : userRepository.findAllByRole(Role.JOB_SEEKER)) {
            //System.out.println("\n\n USER ROLE " + userJobSeeker.getRole().name());
            if (!userJobSeeker.getBlockedUser().getViewTheStatusOfResponded()) {
                // System.out.println("\n USER ROLE " + userJobSeeker.getRole().name());
                if (vacancy.getPosition() != null && userJobSeeker.getJobSeeker() != null && userJobSeeker.getJobSeeker().getProfessions().size() > 0) {
                    // System.out.println("\n\n USER POSITION " + vacancy.getPosition().getName());
                    if (userJobSeeker.getJobSeeker().getProfessions().get(userJobSeeker.getJobSeeker().getProfessions().size() - 1).getPosition() != null) {
                        if (vacancy.getPosition().getName().equals(userJobSeeker.getJobSeeker().getProfessions().get(userJobSeeker.getJobSeeker().getProfessions().size() - 1).getPosition().getName())) {
                            Notification notification = new Notification();
                            notification.setUser(userJobSeeker);
                            notification.setTitle("Новая Вакансия");
                            notification.setArrivedDate(employerService.getTimeLeft(vacancy.getCreationDate().toString()));
                            notification.setContent("Появилась новая вакансия, " +
                                    "соответствующая вашим интересам. " +
                                    "Проверьте подробности и отправьте отклик.");
                            if (vacancy.getEmployer().getResume() != null){
                                notification.setImageId(vacancy.getEmployer().getResume().getId());
                            }
                            notification.setIsRead(false);
                            notification.setIsSend(false);
                            myHandler.notificationSend(user, notification);
                            notifications.add(notification);
                        }
                    }

                }
            }
        }
        vacancyRepository.save(vacancy);
        notificationRepository.saveAll(notifications);

        return vacancyMapper.toDto(vacancyRepository.save(vacancy));
    }


    @Override
    public boolean delete(Long id) {
        Vacancy vacancy = vacancyRepository.findById(id).orElseThrow(() ->
                new NotFoundException("is always dropped or not, the id: " + id, HttpStatus.NOT_FOUND));
        vacancy.getJobSeekers().clear();
        vacancy.setEmployer(null);
        vacancyRepository.delete(vacancy);

        return true;
    }

    @Override
    public List<Vacancy> getAll() {
        return vacancyRepository.findAll();
    }


    @Override
    public String getIdFromSecurity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = "";
        String id = "";
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String s = principal.toString();
//            email = principal.toString().substring(46 + 7, 72 - 3);
            id = principal.toString().substring(5 + 3, 11 - 2);
        }
        return id;
    }

    @Override
    public VacancyResponse update(Long id, VacancyRequest vacancyRequest) {
        Vacancy vacancy = vacancyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Vacancy not found!"));

        vacancyRepository.save(update(vacancy, vacancyRequest));
        return vacancyMapper.toDto(vacancy);

    }

    @Override
    public List<JobSeekerVacanciesResponses> jobSeekerVacancies() {


        List<Vacancy> vacancies = vacancyRepository.findAll();
        return jobSeekerMapper.convertToVacancyJobSeekerResponses(vacancies);
    }

    @Override
    public List<VacancyResponse> getMyVacancies(Long id) {
        return vacancyMapper.toDtos(vacancyRepository.findAllByEmployerId(id));
    }


    public Vacancy update(Vacancy vacancy, VacancyRequest vacancyRequest) {
        if (vacancyRequest.getTypeOfEmploymentS() != null)
            vacancy.setTypeOfEmploymentS(TypeOfEmployment.valueOf(vacancyRequest.getTypeOfEmploymentS()));
        if (vacancyRequest.getAbout_company() != null) {
            vacancy.setAbout_company(vacancyRequest.getAbout_company());
        }
        if (vacancyRequest.getSkills() != null) {
            vacancy.setSkills(vacancyRequest.getSkills());
        }
        if (vacancyRequest.getDescription() != null) {
            vacancy.setDescription(vacancyRequest.getDescription());
        }

        if (vacancyRequest.getPosition() != null) {
            Position position =
                    (positionRepository.findByName(vacancyRequest.getPosition()));
            vacancy.setPosition(position);
            Category category = position.getCategory();
            vacancy.getPosition().setCategory(category);
        }
        if (vacancyRequest.getSalaryResponse() != null) {
            vacancy.setSalary(salaryMapper.toEntity(vacancyRequest.getSalaryResponse()));
        }
        vacancy.setExperience(vacancyRequest.getExperience());
        if (vacancyRequest.getContactInformationResponse() != null) {
            vacancy.setContactInformation(contactInformationMapper.requestToEntity(vacancyRequest.getContactInformationResponse()));
        }
        vacancy.setAdditionalInformation(vacancyRequest.getAdditionalInformation());
        vacancy.setIndustry(vacancyRequest.getIndustry());
        return vacancy;

    }


    @Override
    public List<JobSeekerVacanciesResponses> searchVacancy(String search) {
        List<Vacancy> vacancies;
        String s;
        if (search == null) {
            vacancies = vacancyRepository.findAll();
        } else {
            vacancies = vacancyRepository.search(search.toLowerCase(Locale.ROOT));
            for (Vacancy vacancy : vacancies) {
                if (vacancy.getSearchCounter() == Long.MAX_VALUE) {
                    vacancy.setSearchCounter(vacancy.getSearchCounter() - 1);
                    System.out.println("Long has reached the maximum value");
                }
                vacancy.setSearchCounter(vacancy.getSearchCounter() + 1);
                vacancyRepository.save(vacancy);
            }
        }
        return jobSeekerMapper.convertToVacancyJobSeekerResponses(vacancies);
    }


    @Override
    public List<JobSeekerVacanciesResponses> filter(String category, String position, String country, String city, String experience1,
                                                    String typeOfEmployments, Boolean salary2, Boolean date2) {

        Boolean salary = (salary2 != null) ? Boolean.parseBoolean(salary2.toString()) : null;
        Boolean date = (date2 != null) ? Boolean.parseBoolean(date2.toString()) : null;

        TypeOfEmployment typeOfEmployment = (typeOfEmployments != null && !typeOfEmployments.isEmpty())
                ? TypeOfEmployment.valueOf(typeOfEmployments)
                : null;

        List<Vacancy> all = vacancyRepository.filter2(category, position, country, city, experience1, typeOfEmployment);

        if (salary != null) {
            all.sort(Comparator.comparing((Function<? super Vacancy, ? extends Double>) v -> v.getSalary().getSalarySum(),
                    salary ? Comparator.naturalOrder() : Comparator.reverseOrder()));
        }

        if (date != null) {
            all.sort(Comparator.comparing(Vacancy::getCreationDate,
                    date ? Comparator.naturalOrder() : Comparator.reverseOrder()));
        }

        return jobSeekerMapper.convertToVacancyJobSeekerResponses(all);
    }


    @Override
    public VacancyResponse updateById(Long id, VacancyRequest vacancyRequest) {
        VacancyResponse vacancyResponse =
                vacancyMapper.toDto(vacancyRepository.findById(id).orElseThrow());
        return vacancyResponse;
    }

    @Override
    public VacancyResponse updateEmployerVacancyByIds(Long employerId, Long vacancyId, VacancyRequest vacancyRequest) {
        Employer employer = employeeRepository.findById(employerId).orElseThrow();
        Vacancy vacancy = employer.getVacancyList().get(Math.toIntExact(vacancyId));
        //vacancyRequest = vacancyMapper.toDto(vacancy);
        return vacancyMapper.requestToResponse(vacancyRequest);
    }

    @Override
    public Response uploadImage(MultipartFile file, Long id) throws IOException {
        Vacancy vacancy = vacancyRepository.findById(id).orElseThrow(() -> new NotFoundException("employer not found!", HttpStatus.NOT_FOUND));
        if (vacancy.getResume() != null) {
            FileData image = vacancy.getResume();
            vacancy.setResume(null);
            FileData save = fileDataService.uploadFile(file, image);
            vacancy.setResume(save);
            vacancyRepository.save(vacancy);
        } else {
            FileData image = fileDataService.uploadFile(file);
            vacancy.setResume(image);
            vacancyRepository.save(vacancy);
        }

        return null;
    }

    @Override
    public VacancyResponse responded(Long vacancyId, Long userId) {

        Long jobSeekerId = userRepository.findById(userId).get().getJobSeeker().getId();
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElseThrow(() -> new EntityNotFoundException("Vacancy not found"));
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElseThrow(() -> new EntityNotFoundException("jobSeeker not found: " + jobSeekerId));

        List<JobSeeker> jobSeekers = new ArrayList<>();
        if (!vacancy.getJobSeekers().contains(jobSeeker)) {


            if (vacancy.getJobSeekers().isEmpty()) {

                jobSeekers.add(jobSeeker);
                vacancy.setJobSeekers(jobSeekers);

                vacancy.setResponse(vacancy.getJobSeekers().size());
                JobSeekerVacancyInformation jobSeekerVacancyInformation = new JobSeekerVacancyInformation();
                jobSeekerVacancyInformation.setUserId(jobSeeker.getUser().getId());
                jobSeekerVacancyInformation.setJobSeekerId(jobSeeker.getId());
                jobSeekerVacancyInformation.setVacancyId(vacancy.getId());
                jobSeekerVacancyInformation.setLocalDateTime(LocalDateTime.now());
                jobSeekerVacancyInformation.setDays(employerService.getTimeLeft2(LocalDateTime.now()));
                jobSeekerVacancyInformation.setStatusOfJobSeeker(StatusOfJobSeeker.отправлено);
                jobSeekerVacancyInformationRepository.save(jobSeekerVacancyInformation);

                vacancyRepository.save(vacancy);
            } else {
                for (JobSeeker jobSeeker1 : vacancy.getJobSeekers()) {
                    if (!Objects.equals(jobSeeker1.getId(), jobSeeker1)) {
                        vacancy.getJobSeekers().add(jobSeeker);
                        vacancy.setResponse(vacancy.getJobSeekers().size());
                        JobSeekerVacancyInformation jobSeekerVacancyInformation = new JobSeekerVacancyInformation();
                        jobSeekerVacancyInformation.setUserId(jobSeeker.getUser().getId());
                        jobSeekerVacancyInformation.setJobSeekerId(jobSeeker.getId());
                        jobSeekerVacancyInformation.setVacancyId(vacancy.getId());
                        jobSeekerVacancyInformation.setDays(employerService.getTimeLeft2(LocalDateTime.now()));
                        jobSeekerVacancyInformation.setStatusOfJobSeeker(StatusOfJobSeeker.отправлено);
                        jobSeekerVacancyInformation.setLocalDateTime(LocalDateTime.now());
                        jobSeekerVacancyInformationRepository.save(jobSeekerVacancyInformation);
                        vacancyRepository.save(vacancy);
                    }
                    break;
                }
            }

            Notification notification = new Notification();
            System.out.println("Notification notification = new Notification()");
            User userEmployer = vacancy.getEmployer() != null ? vacancy.getEmployer().getUser() : null;
            System.out.println(vacancy.getEmployer() != null);
            if (userEmployer != null) {
                System.out.println("\n llll" + userEmployer);
                notification.setUser(userEmployer);
                notification.setTitle("Новая Заявка");
                notification.setArrivedDate(employerService.getTimeLeft2(LocalDateTime.now()));
                notification.setContent(jobSeeker.getLastname() + " откликнулся на вакансию " + vacancy.getPosition().getName());

                if(jobSeeker.getImage() != null){
                    notification.setImageId(jobSeeker.getImage().getId());
                }

                notification.setIsRead(false);
                notification.setIsSend(false);
                notificationRepository.save(notification);
                myHandler.notificationSend(userEmployer, notification);
            }

        }
        return vacancyMapper.toDto(vacancy);
    }

    @Override
    public void setStatusOfJobSeeker(Long vacancyId, Long jobSeekerId, String status) {

        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElseThrow(() -> new NotFoundException("Vacancy not found!" + vacancyId, HttpStatus.NOT_FOUND));
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId).orElseThrow(() -> new NotFoundException("Job Seeker not found!", HttpStatus.NOT_FOUND));

        if (vacancy.getJobSeekers().contains(jobSeeker)) {
            System.out.println("yes");
            jobSeeker.setStatusOfJobSeeker(
                    employerService.containsStatusOfJobSeeker(status) ?
                            StatusOfJobSeeker.valueOf(status) : null);
            jobSeeker.setUserApplicationDate(LocalDateTime.now());
            jobSeekerRepository.save(jobSeeker);
            JobSeekerVacancyInformation jobSeekerVacancyInformation =
                    jobSeekerVacancyInformationRepository.findByJobSeekerIdAndVacancyId(
                            jobSeekerId, vacancyId
                    );
            jobSeekerVacancyInformation.setStatusOfJobSeeker(StatusOfJobSeeker.valueOf(status));

            jobSeekerVacancyInformationRepository.save(jobSeekerVacancyInformation);

            Notification notification = new Notification();
            notification.setUser(jobSeeker.getUser());
            notification.setTitle("СОБЕСЕДОВАНИЕ");
            if (jobSeeker.getStatusOfJobSeeker().equals(StatusOfJobSeeker.собеседование)) {
                notification.setContent("Вас пригласили на собеседование в " +
                        vacancy.getEmployer().getCompanyName() + " Подготовьтесь к встрече и ознакомьтесь с деталями.");

            } else if (jobSeeker.getStatusOfJobSeeker().equals(StatusOfJobSeeker.рассматривается)) {
                notification.setContent("Вы в расмотрении");
            } else if (jobSeeker.getStatusOfJobSeeker().equals(StatusOfJobSeeker.принято)) {
                notification.setContent("УРА вас приняли на работу");
            } else if (jobSeeker.getStatusOfJobSeeker().equals(StatusOfJobSeeker.отклонено)) {
                notification.setContent("Вас отклонили");
            }

            if (vacancy.getEmployer().getResume() != null){
                notification.setImageId(vacancy.getEmployer().getResume().getId());
            }
            notification.setIsRead(false);
            notification.setIsSend(false);
            notification.setArrivedDate(employerService.getTimeLeft2(LocalDateTime.now()));
            notificationRepository.save(notification);
            myHandler.notificationSend(vacancy.getEmployer().getUser(), notification);
        } else
            System.out.println("no");
    }


    @Override
    public void setStatusOfVacancy(Long id, String statusOfVacancy) {
        Vacancy vacancy = vacancyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Vacancy not found!"));
        System.out.println("status " + statusOfVacancy);
//        if (statusOfVacancy.equals("Открыто")){
//            statusOfVacancy = "Open";
//        }
//        if (statusOfVacancy.equals("В архиве")){
//            statusOfVacancy = "Archive";
//        }
//        if (statusOfVacancy.equals("Закрыто")){
//            statusOfVacancy = "Closed";
//        }
        System.out.println("1)"+statusOfVacancy);
        StatusOfVacancy statusOfVacancy1 = StatusOfVacancy.getByRussianTranslation(statusOfVacancy);
        System.out.println(statusOfVacancy1.name());
        vacancy.setStatusOfVacancy(statusOfVacancy1);


        for (JobSeeker jobSeeker : vacancy.getJobSeekers()) {
            Notification notification = new Notification();
            if (jobSeeker.getUser() != null) {
                User user = jobSeeker.getUser();
                notification.setUser(user);
                notification.setTitle("Объявление!!");
                notification.setContent("Company " + vacancy.getEmployer().getCompanyName() +
                        " change status vacancy of " + StatusOfVacancy.valueOf(statusOfVacancy));
                if (vacancy.getEmployer().getResume() != null){
                    notification.setImageId(vacancy.getEmployer().getResume().getId());
                }
                notification.setIsRead(false);
                notification.setIsSend(false);
                notification.setArrivedDate(employerService.getTimeLeft2(LocalDateTime.now()));
                notificationRepository.save(notification);
                myHandler.notificationSend(vacancy.getEmployer().getUser(), notification);
            }
        }

        vacancyRepository.save(vacancy);
    }

    @Override
    public List<RespondedResponse> listForResponded(Long vacancyId) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElseThrow(() -> new EntityNotFoundException("Vacancy not found"));
        return jobSeekerMapper.toDtosForListResponded(vacancy.getJobSeekers(), vacancyId);
    }

    @Override
    public List<RespondedResponse> listForResponded(Long vacancyId, List<JobSeeker> jobSeekers) {
        Optional<Vacancy> vacancyOptional = vacancyRepository.findById(vacancyId);
        if (vacancyOptional.isEmpty()) {
            return null; // or handle the vacancy not found case as needed
        }
        List<JobSeeker> jobSeekers1 = (vacancyRepository.findById(vacancyId)).orElseThrow(() -> new EntityNotFoundException("Vacancy not found")).getJobSeekers();
        jobSeekers.retainAll(jobSeekers1);
        return jobSeekerMapper.toDtosForListResponded(jobSeekers, vacancyId);
    }

    @Override
    public List<RespondedResponse> listForResponded(
            Long vacancyId, String statusOfJobSeeker,
            String experience, String applicationDate) {
        StatusOfJobSeeker statusOfJobSeeker1 = StatusOfJobSeeker.valueOf(statusOfJobSeeker);
        Experience experience1 = experienceRepository.findByName(experience).get();


        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;
        LocalDateTime end;


        switch (applicationDate) {
            case "TODAY":
                start = now.with(LocalTime.MIN);
                end = now.with(LocalTime.MAX);
                break;
            case "THIS_WEEK":
                start = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
                end = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);
                break;
            case "THIS_MONTH":
                start = now.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
                end = now.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
                break;
            case "THIS_YEAR":
                start = now.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
                end = now.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
                break;
            default:
                throw new IllegalArgumentException("Invalid time period");
        }
        List<JobSeeker> jobSeekers2 = jobSeekerRepository.findByUserApplicationDateRange(start, end);
        List<JobSeeker> jobSeekers = (vacancyRepository.findById(vacancyId)).orElseThrow(() -> new EntityNotFoundException("Vacancy not found")).getJobSeekers();


        List<JobSeeker> jobSeekers1 = jobSeekerRepository.findByStatusOfJobSeekerAndExperienceJAndUserApplicationDate(
                statusOfJobSeeker1, experience1, null);

        jobSeekers1.retainAll(jobSeekers);
        return jobSeekerMapper.toDtosForListResponded(jobSeekers2, vacancyId);
    }

    @Override
    public AboutVacancy aboutVacancy(Long vacancyId) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElseThrow(() ->
                new NotFoundException("The vacancy not found with id: " + vacancyId, HttpStatus.NOT_FOUND));
        return vacancyMapper.toAboutVacancyResponse(vacancy);
    }

    @Override
    public List<VacancyResponse> sortedTwoVacancies(Long vacancyId) {
        Vacancy inVacancy = vacancyRepository.findById(vacancyId).orElseThrow();
        String positionName = inVacancy.getPosition().getName();
        String categoryName = inVacancy.getPosition().getCategory().getName();  // Assuming category is within Position
        String country = inVacancy.getContactInformation().getCountry();
        String city = inVacancy.getContactInformation().getCity();
        String experience = inVacancy.getExperience();

        List<Vacancy> vacancies = vacancyRepository.findSimilarOrSameVacancies(
                positionName, categoryName, country, city, experience, inVacancy.getId()
        );

        vacancies.sort(Comparator.comparing(Vacancy::getCreationDate));

        return vacancyMapper.toDtos(vacancies.stream().limit(2).collect(Collectors.toList()));
    }

    @Override
    public List<Vacancy> respondedJobseekersSearch(String search) {
        List<JobSeekerVacancyInformation> jobSeekerVacancyInformations = new ArrayList<>();
        List<Vacancy> vacancies = new ArrayList<>();
        if (search == null) {
            jobSeekerVacancyInformations = jobSeekerVacancyInformationRepository.findAll();
        } else {
            jobSeekerVacancyInformations
                    = jobSeekerVacancyInformationRepository.findAll();
        }
        for (JobSeekerVacancyInformation j : jobSeekerVacancyInformations) {
            vacancies.add(vacancyRepository.findById(j.getVacancyId()).get());
        }

        return vacancies;
    }

    public List<RespondedVacancyResponse> respondedJobseekersFilterByDaysAndStatus(String days, JobSeeker jobSeeker, String statusOfJobSeeker, Long userId) {
        if (statusOfJobSeeker.isEmpty() && days.isEmpty()) {
            List<JobSeekerVacancyInformation> jobSeekerVacancyInformations =
                    jobSeekerVacancyInformationRepository.findAllByUserId(userId);
            return jobSeekerVacancyInformationMapper.toRespondedVacancyResponseFromInformS(jobSeekerVacancyInformations);

        }
        List<JobSeekerVacancyInformation> jobSeekerVacancyInformations = new ArrayList<>();
        if (days.isEmpty() && !statusOfJobSeeker.isEmpty()) {
            jobSeekerVacancyInformations = jobSeekerVacancyInformationRepository.findAllByStatusOfJobSeekerAndUserId(StatusOfJobSeeker.valueOf(statusOfJobSeeker), userId);

        } else if (statusOfJobSeeker.isEmpty() && !days.isEmpty()) {
            jobSeekerVacancyInformations = jobSeekerVacancyInformationRepository.findAllByUserIdAndDays(userId, days);
        } else
            jobSeekerVacancyInformations = jobSeekerVacancyInformationRepository.findAllByStatusOfJobSeekerAndUserIdAndDays(StatusOfJobSeeker.valueOf(statusOfJobSeeker), userId, days);
        for (JobSeekerVacancyInformation jobSeekerVacancyInformation : jobSeekerVacancyInformations) {
            jobSeekerVacancyInformation.setDays(employerService.getTimeLeft2((jobSeekerVacancyInformation.getLocalDateTime())));
            jobSeekerVacancyInformationRepository.save(jobSeekerVacancyInformation);
        }


        return jobSeekerVacancyInformationMapper.toRespondedVacancyResponseFromInformS(jobSeekerVacancyInformations);

    }

    @Override
    public List<VacancyResponse> employerVacanciesSearchUserId(Long userId, String search) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User not found!" + userId, HttpStatus.NOT_FOUND));
        Long employerId = user.getEmployer().getId();
        List<Vacancy> vacancies = vacancyRepository.findAllByEmployerId(employerId);
        List<Vacancy> vacancies1 = new ArrayList<>();

        if (search == null) {
            vacancies1 = vacancyRepository.findAll();
        } else {
            vacancies1 = vacancyRepository.search(search.toLowerCase(Locale.ROOT));
        }
        List<Vacancy> result = new ArrayList<>();
        for (Vacancy vacancy : vacancies1) {
            if (vacancies.contains(vacancy))
                result.add(vacancy);
        }

        return vacancyMapper.toDtos(result);


    }

    public List<VacancyResponse> employerVacanciesFilterUserId(Long userId, String respondedCount, String byDate, String byStatusOfVacancy) {
        Employer employer = userRepository.findById(userId).get().getEmployer();
        List<Vacancy> vacancies = vacancyRepository.findAllByEmployerId(employer.getId());
        for (Vacancy v : vacancies) {
            v.setDays(employerService.getTimeLeft2(v.getCreationDate()));
            vacancyRepository.save(v);
        }

        List<Vacancy> all = filterByRespondedCountAndByDateAndByStatusOfvacancy(employer.getId(), vacancies, respondedCount, byDate, byStatusOfVacancy);

        return vacancyMapper.toDtos(all);
    }

    private List<Vacancy> filterByRespondedCountAndByDateAndByStatusOfvacancy(Long id, List<Vacancy> vacancies, String respondedCount, String byDate, String byStatusOfVacancy) {
        for (Vacancy v : vacancyRepository.findAll()) {
            v.setDays(employerService.getTimeLeft2(v.getCreationDate()));
            vacancyRepository.save(v);
        }
        int min = 0, max = 0;
        if (respondedCount.equals("0 - 20")) {
            min = 0;
            max = 20;
        } else if (respondedCount.equals("21 - 50")) {
            min = 21;
            max = 50;
        } else {
            min = 51;
            max = 9999;
        }

        if (byStatusOfVacancy == null || byStatusOfVacancy.isEmpty()) {
            return vacancyRepository.findAllByParameters(min, max, id, null, byDate);

        }
        System.out.println(byStatusOfVacancy);

        StatusOfVacancy statusOfVacancy =
                StatusOfVacancy.getByRussianTranslation(byStatusOfVacancy);
        String st = statusOfVacancy != null ? statusOfVacancy.name() : null;
        return vacancyRepository.findAllByParameters(min, max, id, st, byDate);
    }

    @Override
    public List<VacancyResponsesForAdmin> getAllVacancy(String vacancyName, String filterType) {

        if (vacancyName == null || vacancyName.isEmpty() && filterType.isEmpty() || filterType == null) {
            return vacancyMapper.toDtosVacanciesForAdmin(vacancyRepository.findAll());
        } else if (vacancyName != null && filterType.isEmpty()) {
            return vacancyMapper.toDtosVacanciesForAdmin(vacancyRepository.searchVacancyByName(vacancyName));
        } else {
            return vacancyMapper.toDtosVacanciesForAdmin(vacancyRepository.searchVacancyByNameAndDateFilter(vacancyName, filterType));
        }
    }

    @Override
    public List<VacancyResponseForPopularCategories> getVacancyCountByCategory() {
        Map<String, Integer> categoryCountMap = new HashMap<>();

        for (Vacancy vacancy : vacancyRepository.findAll()) {
            String category = vacancy.getPosition().getCategory().getName();
            categoryCountMap.put(category, categoryCountMap.getOrDefault(category, 0) + 1);
        }

        return categoryCountMap.entrySet().stream()
                .map(entry -> new VacancyResponseForPopularCategories(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<VacancyResponseForPopularCategories> getPopularPosition() {
        Map<String, Integer> popularCategory = new HashMap<>();

        for (Position position : positionRepository.findAll()) {
            popularCategory.put(position.getCategory().getName(), position.getVacancies().size());
        }
         return popularCategory.entrySet().stream()
                .filter(entry -> entry.getValue() != null) // Filter out entries with null values
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(entry -> new VacancyResponseForPopularCategories(entry.getKey(), entry.getValue()))
                .toList();
    }

}
