package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.contactInformation.ContactInformationRequest;
import com.example.hr_system.dto.contactInformation.ContactInformationResponse;
import com.example.hr_system.entities.ContactInformation;
import com.example.hr_system.mapper.ContactInformationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ContactInformationMapperImpl implements ContactInformationMapper {
    @Override
    public ContactInformationResponse toDto(ContactInformation contactInformation) {
        if (contactInformation== null){
            return null;
        }
        ContactInformationResponse response= new ContactInformationResponse();
        response.setIdl(contactInformation.getId());
        response.setCity(contactInformation.getCity());
        response.setCountry(contactInformation.getCountry());
        response.setStreet_house(contactInformation.getStreet_house());
        return response;
    }

    @Override
    public List<ContactInformationResponse> toDto(List<ContactInformation> contactInformations) {
        List<ContactInformationResponse> contactInformationResponses=new ArrayList<>();
        for (ContactInformation contactInformation:contactInformations) {
            contactInformationResponses.add(toDto(contactInformation));
        }
        return contactInformationResponses;
    }

    @Override
    public ContactInformationResponse requestToresponse(ContactInformationRequest contactInformationRequest) {
        if(contactInformationRequest!=null) {


            ContactInformationResponse contactInformationResponse = new ContactInformationResponse();
            contactInformationResponse.setCity(contactInformationRequest.getCity());
            contactInformationResponse.setCountry(contactInformationRequest.getCountry());
            contactInformationResponse.setStreet_house(contactInformationRequest.getStreet_house());
            return contactInformationResponse;
        }
    return null;
    }

    @Override
    public ContactInformation requestToEntity(ContactInformationRequest contactInformationRequest) {
        ContactInformation contactInformation = new ContactInformation();
        contactInformation.setCity(contactInformationRequest.getCity());
        contactInformation.setCountry(contactInformationRequest.getCountry());
        contactInformation.setStreet_house(contactInformationRequest.getStreet_house());
        return contactInformation;
    }
}
