package com.example.hr_system.repository;

import com.example.hr_system.entities.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileData, Long> {

    FileData findByName(String name);
}
