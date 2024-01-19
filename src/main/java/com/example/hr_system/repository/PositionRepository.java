package com.example.hr_system.repository;

import com.example.hr_system.entities.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position,Long> {
    Position findByName(String name);

}
