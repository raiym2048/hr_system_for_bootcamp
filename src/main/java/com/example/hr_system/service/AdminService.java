package com.example.hr_system.service;

import com.example.hr_system.dto.GenericResponseForUserResponses;
import com.example.hr_system.dto.admin.ResponsesForAdmin;
import com.example.hr_system.dto.admin.ResponsesForSupport;
import com.example.hr_system.dto.admin.SupportRequest;
import com.example.hr_system.dto.vacancy.VacancyResponsesForAdmin;
import com.example.hr_system.entities.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AdminService {
    List<ResponsesForAdmin> getAllUsers(String name, String userRole);

    GenericResponseForUserResponses getResponsesForUserById(Long userId);

    GenericResponseForUserResponses setRoleForUser(Long userId, String role);

    boolean deleteByAccount(String email);

    boolean deleteByAccount(String email, String password);

    boolean listForDeletingUsers(List<Long> selectedUserIds);
    boolean listForDeletingVacancy(List<Long> selectedVacancyIds);

    boolean userBlocked(Long userId, Boolean aBoolean);

    void block(String email);

    User isAuth(Authentication authentication);

    List<ResponsesForSupport> getListSupport();

    ResponsesForSupport createSupport(SupportRequest request);

    boolean deleteSupportById(Long supportId);
}
