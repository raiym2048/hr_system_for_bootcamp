package com.example.hr_system.repository;

import com.example.hr_system.entities.BlockedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedRepository extends JpaRepository<BlockedUser, Long> {
}
