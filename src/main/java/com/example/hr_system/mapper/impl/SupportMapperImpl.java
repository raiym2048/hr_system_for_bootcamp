package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.admin.ResponsesForSupport;
import com.example.hr_system.entities.ListSupport;
import com.example.hr_system.mapper.SupportMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class SupportMapperImpl implements SupportMapper {
    @Override
    public ResponsesForSupport toDto(ListSupport listSupport) {
        if (listSupport == null) {
            return null;
        }

        ResponsesForSupport forSupport = new ResponsesForSupport();
        forSupport.setId(listSupport.getId());
        forSupport.setPersonName(listSupport.getPersonName());
        forSupport.setPersonEmail(listSupport.getPersonEmail());
        forSupport.setPersonPhoneNumber(listSupport.getPersonPhoneNumber());
        forSupport.setMassage(listSupport.getMessage());
        forSupport.setDateSent(listSupport.getDateSent());

        return forSupport;
    }

    @Override
    public List<ResponsesForSupport> toDtos(List<ListSupport> supports) {

        List<ResponsesForSupport> responsesForSupports = new ArrayList<>();
        for (ListSupport listSupport : supports){
            responsesForSupports.add(toDto(listSupport));
        }
        return responsesForSupports;
    }
}
