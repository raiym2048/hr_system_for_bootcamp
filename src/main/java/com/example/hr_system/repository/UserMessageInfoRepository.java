package com.example.hr_system.repository;

import com.example.hr_system.entities.UserMessageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMessageInfoRepository extends JpaRepository<UserMessageInfo, Long> {
    Optional<UserMessageInfo> findByEmail(String email);
}
