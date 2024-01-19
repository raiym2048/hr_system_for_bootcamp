package com.example.hr_system.service;

import com.example.hr_system.dto.admin.BlockedUserRequest;
import com.example.hr_system.dto.admin.BlockedUserResponses;
import com.example.hr_system.dto.vacancy.VacancyResponse;
import com.example.hr_system.entities.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BlockedUserService {
    BlockedUserResponses isBlockedUser(Long userId, BlockedUserRequest request);

    User getUsernameFromToken(String token);
}
