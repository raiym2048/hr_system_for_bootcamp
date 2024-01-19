package com.example.hr_system.mapper;

import com.example.hr_system.dto.admin.BlockedUserResponses;
import com.example.hr_system.entities.BlockedUser;
import com.example.hr_system.entities.JobSeeker;
import com.example.hr_system.entities.User;

public interface BlockedUserMapper {
    BlockedUserResponses toDto(BlockedUser blockedUser);
}
