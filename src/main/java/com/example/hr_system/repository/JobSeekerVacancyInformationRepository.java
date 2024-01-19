package com.example.hr_system.repository;

import com.example.hr_system.entities.JobSeekerVacancyInformation;
import com.example.hr_system.enums.StatusOfJobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerVacancyInformationRepository extends JpaRepository<JobSeekerVacancyInformation,Long> {
    JobSeekerVacancyInformation findByJobSeekerIdAndVacancyId(Long jobSeekerId, Long vacancyId);

    List<JobSeekerVacancyInformation> findAllByStatusOfJobSeeker (StatusOfJobSeeker statusOfJobSeeker);
    List<JobSeekerVacancyInformation> findAllByStatusOfJobSeekerAndUserId (StatusOfJobSeeker statusOfJobSeeker, Long userId);
    List<JobSeekerVacancyInformation> findAllByUserId (Long userId);
    List<JobSeekerVacancyInformation> findAllByStatusOfJobSeekerAndUserIdAndDays (StatusOfJobSeeker statusOfJobSeeker, Long userId, String days);
    List<JobSeekerVacancyInformation> findAllByUserIdAndDays (Long userId, String days);
}
