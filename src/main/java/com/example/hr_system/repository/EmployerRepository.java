package com.example.hr_system.repository;


import com.example.hr_system.entities.Employer;
import com.example.hr_system.entities.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
    Employer findByEmail(String email);

}
