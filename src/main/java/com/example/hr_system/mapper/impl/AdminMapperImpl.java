package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.admin.ResponsesForAdmin;
import com.example.hr_system.entities.Employer;
import com.example.hr_system.entities.JobSeeker;
import com.example.hr_system.entities.User;
import com.example.hr_system.enums.Role;
import com.example.hr_system.mapper.AdminMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class AdminMapperImpl implements AdminMapper {

    @Override
    public ResponsesForAdmin toDto(User user) {
            if (user == null) {
                return null;
            }

            ResponsesForAdmin forAdmin = new ResponsesForAdmin();
            forAdmin.setUserId(user.getId());
            if (user.getEmployer() != null){
                forAdmin.setUserName(user.getEmployer().getCompanyName());
            }else if (user.getJobSeeker() != null){
                forAdmin.setUserName(user.getJobSeeker().getFirstname() + " " + user.getJobSeeker().getLastname());
            }
            forAdmin.setUserRole(user.getRole().name());
            forAdmin.setUserEmail(user.getEmail());
            forAdmin.setLastVisit(user.getLastVisit());
            return forAdmin;
        }

    @Override
    public List<ResponsesForAdmin> toDtos(List<User> users) {

        List<ResponsesForAdmin> responsesMainListForAdmins = new ArrayList<>();
        for (User user :users) {
            responsesMainListForAdmins.add(toDto(user));
        }
        return responsesMainListForAdmins;
    }

    @Override
    public ResponsesForAdmin toDtoEmployer(Employer employer) {
        if (employer == null) {
            return null;
        }

        ResponsesForAdmin forAdminEmployer = new ResponsesForAdmin();
        forAdminEmployer.setUserId(employer.getId());
        forAdminEmployer.setUserName(employer.getCompanyName());
        forAdminEmployer.setUserRole(employer.getUser().getRole().name());
        forAdminEmployer.setUserEmail(employer.getEmail());
        forAdminEmployer.setLastVisit(employer.getUser().getLastVisit());
        return forAdminEmployer;
    }

    @Override
    public List<ResponsesForAdmin> toDtosEmployer(List<Employer> employers) {
        List<ResponsesForAdmin> responsesMainListForAdminsEmployers = new ArrayList<>();
        for (Employer employer :employers) {
            responsesMainListForAdminsEmployers.add(toDtoEmployer(employer));
        }
        return responsesMainListForAdminsEmployers;
    }


    @Override
    public ResponsesForAdmin toDtoJobSeeker(JobSeeker jobSeeker) {
        if (jobSeeker == null) {
            return null;
        }

        ResponsesForAdmin forAdminJobSeeker = new ResponsesForAdmin();
        forAdminJobSeeker.setUserId(jobSeeker.getId());
        forAdminJobSeeker.setUserName(jobSeeker.getFirstname() + " " + jobSeeker.getLastname());
        forAdminJobSeeker.setUserRole(jobSeeker.getUser().getRole().name());
        forAdminJobSeeker.setUserEmail(jobSeeker.getEmail());
        forAdminJobSeeker.setLastVisit(jobSeeker.getUser().getLastVisit());
        return forAdminJobSeeker;
    }

    @Override
    public List<ResponsesForAdmin> toDtosJobSeekers(List<JobSeeker> jobSeekers) {
        List<ResponsesForAdmin> responsesMainListForAdminsJobSeekers = new ArrayList<>();
        for (JobSeeker jobSeeker :jobSeekers) {
            responsesMainListForAdminsJobSeekers.add(toDtoJobSeeker(jobSeeker));
        }
        return responsesMainListForAdminsJobSeekers;
    }
}
