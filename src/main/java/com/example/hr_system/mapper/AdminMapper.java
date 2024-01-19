package com.example.hr_system.mapper;

import com.example.hr_system.dto.admin.ResponsesForAdmin;
import com.example.hr_system.dto.jobSeeker.JobSeekerResponses;
import com.example.hr_system.entities.Employer;
import com.example.hr_system.entities.JobSeeker;
import com.example.hr_system.entities.User;

import java.util.List;

public interface AdminMapper {
    ResponsesForAdmin toDto(User user);

    List<ResponsesForAdmin> toDtos(List<User> users);

    ResponsesForAdmin toDtoEmployer(Employer employer);
    List<ResponsesForAdmin> toDtosEmployer(List<Employer> employers);

    ResponsesForAdmin toDtoJobSeeker(JobSeeker jobSeeker);
    List<ResponsesForAdmin> toDtosJobSeekers(List<JobSeeker> jobSeekers);

}


