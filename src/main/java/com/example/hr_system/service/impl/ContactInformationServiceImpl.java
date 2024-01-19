package com.example.hr_system.service.impl;

import com.example.hr_system.dto.contactInformation.ContactInformationRequest;
import com.example.hr_system.entities.ContactInformation;
import com.example.hr_system.service.ContactInfromationService;
import org.springframework.stereotype.Service;


@Service
public class ContactInformationServiceImpl implements ContactInfromationService {

    @Override
    public ContactInformation convertToEntity(ContactInformationRequest contactInformationRequest) {
        if (contactInformationRequest == null) {
            return null;
        }
        ContactInformation contactInformation = new ContactInformation();
        contactInformation.setCity(contactInformationRequest.getCity());
        contactInformation.setCountry(contactInformationRequest.getCountry());
        contactInformation.setStreet_house(contactInformationRequest.getStreet_house());
        return contactInformation;
    }

}
