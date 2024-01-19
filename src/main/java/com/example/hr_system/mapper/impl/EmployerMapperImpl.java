package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.employer.EmployerResponses;
import com.example.hr_system.entities.Employer;
import com.example.hr_system.exception.NotFoundException;
import com.example.hr_system.mapper.EmployerMapper;
import com.example.hr_system.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployerMapperImpl implements EmployerMapper {

    private final FileMapper fileMapper;
    @Override
    public EmployerResponses toDto(Employer employer) {
        if (employer == null) {
            throw new NotFoundException("employer is null!", HttpStatus.NOT_FOUND);
        }
        EmployerResponses employerResponses = new EmployerResponses();
        if (employer.getId() != null) {
            employerResponses.setId(employer.getId());
        }
        employerResponses.setFileResponse(
                fileMapper.toDto(employer.getResume()));
        employerResponses.setId(employer.getId());

        employerResponses.setCompanyName(employer.getCompanyName());
        employerResponses.setAboutCompany(employer.getAboutCompany());
        employerResponses.setCountry(employer.getCountry());
        employerResponses.setCity(employer.getCity());
        employerResponses.setAddress(employer.getAddress());
        employerResponses.setEmail(employer.getEmail());
        employerResponses.setRole(employer.getUser().getRole());
        employerResponses.setPhoneNumber(employer.getPhoneNumber());
        if (employer.getUser().getBlockedUser() != null){
            employerResponses.setCommunicationWithJobSeekers(employer.getUser().getBlockedUser().getCommunicationWithJobSeekers());
            employerResponses.setVacancyAndHiringManagement(employer.getUser().getBlockedUser().getVacancyAndHiringManagement());
            employerResponses.setViewingCandidateData(employer.getUser().getBlockedUser().getViewingCandidateData());
        }
        employerResponses.setBlocked(employer.getUser().getBlocked());

        return employerResponses;
    }

    @Override
    public List<EmployerResponses> toDtos(List<Employer> employerList) {
        List<EmployerResponses>vacancyResponses=new ArrayList<>();
        for (Employer vacancy:employerList) {
            vacancyResponses.add(toDto(vacancy));
        }

        return vacancyResponses;
    }
}
