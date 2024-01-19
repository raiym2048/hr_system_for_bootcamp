package com.example.hr_system.mapper;

import com.example.hr_system.dto.contactInformation.ContactInformationRequest;
import com.example.hr_system.dto.contactInformation.ContactInformationResponse;
import com.example.hr_system.entities.ContactInformation;

import java.util.List;

public interface ContactInformationMapper {

    ContactInformationResponse toDto(ContactInformation contactInformation);

    List<ContactInformationResponse> toDto(List<ContactInformation> contactInformation);

    ContactInformationResponse requestToresponse(ContactInformationRequest contactInformationRequest);

    ContactInformation requestToEntity(ContactInformationRequest contactInformationRequest);
}
