package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.admin.BlockedUserRequest;
import com.example.hr_system.dto.admin.BlockedUserResponses;
import com.example.hr_system.dto.jobSeeker.JobSeekerRequest;
import com.example.hr_system.dto.position.PositionRequest;
import com.example.hr_system.dto.position.PositionResponse;
import com.example.hr_system.entities.BlockedUser;
import com.example.hr_system.entities.JobSeeker;
import com.example.hr_system.entities.User;
import com.example.hr_system.mapper.BlockedUserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class BlockedUserMapperImpl implements BlockedUserMapper {

    @Override
    public BlockedUserResponses toDto(BlockedUser blockedUser) {
        BlockedUserResponses blockedUserResponses = new BlockedUserResponses();
        blockedUserResponses.setUserId(blockedUser.getId());
        blockedUserResponses.setViewingCandidateData(blockedUser.getViewingCandidateData());
        blockedUserResponses.setVacancyAndHiringManagement(blockedUser.getVacancyAndHiringManagement());
        blockedUserResponses.setViewTheStatusOfResponded(blockedUser.getViewTheStatusOfResponded());
        blockedUserResponses.setCommunicationWithEmployers(blockedUser.getCommunicationWithEmployers());
        blockedUserResponses.setCommunicationWithJobSeekers(blockedUser.getCommunicationWithJobSeekers());
        blockedUserResponses.setViewingAndSearchingForVacancies(blockedUser.getViewingAndSearchingForVacancies());
        blockedUserResponses.setViewingCompanyData(blockedUser.getViewingCompanyData());
        return blockedUserResponses;
    }
}
