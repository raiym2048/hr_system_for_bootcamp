package com.example.hr_system.repository;

import com.example.hr_system.entities.Profession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Long> {
    List<Profession> findAllByJobSeekerId(Long id);
}
