package com.example.hr_system.entities;

import com.example.hr_system.enums.ApplicationDate;
import com.example.hr_system.enums.StatusOfJobSeeker;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class JobSeekerVacancyInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long jobSeekerId;
    private Long vacancyId;
    @Enumerated(EnumType.STRING)
    private StatusOfJobSeeker statusOfJobSeeker;
    private LocalDateTime localDateTime = LocalDateTime.now();
    private String days;

}
