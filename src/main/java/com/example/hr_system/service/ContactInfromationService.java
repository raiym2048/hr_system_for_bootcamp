package com.example.hr_system.service;

import com.example.hr_system.dto.contactInformation.ContactInformationRequest;
import com.example.hr_system.dto.contactInformation.ContactInformationResponse;
import com.example.hr_system.entities.ContactInformation;

public interface ContactInfromationService {
    ContactInformation convertToEntity(ContactInformationRequest contactInformationRequest);

}
