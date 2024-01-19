package com.example.hr_system.repository;


import com.example.hr_system.entities.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience,Long> {
    Optional<Experience> findByName(String name);

}
