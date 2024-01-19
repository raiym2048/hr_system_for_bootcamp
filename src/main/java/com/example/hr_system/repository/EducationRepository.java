package com.example.hr_system.repository;

import com.example.hr_system.entities.EducationJob;
import com.example.hr_system.enums.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<EducationJob, Long> {
    List<EducationJob> findAllByJobSeekerId(Long id);
}
