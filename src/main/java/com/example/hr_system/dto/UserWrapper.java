package com.example.hr_system.dto;

import com.example.hr_system.entities.Employer;
import com.example.hr_system.entities.JobSeeker;
import com.example.hr_system.entities.User;
import lombok.Data;

@Data
public class UserWrapper {
    private Employer employer;
    private JobSeeker jobSeeker;
    private User user;

    public UserWrapper(Employer employer) {
        this.employer = employer;
    }

    public UserWrapper(JobSeeker jobSeeker) {
        this.jobSeeker = jobSeeker;
    }

    public UserWrapper(User user) {
        this.user = user;
    }
}

