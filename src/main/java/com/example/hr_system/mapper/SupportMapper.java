package com.example.hr_system.mapper;

import com.example.hr_system.dto.admin.ResponsesForSupport;
import com.example.hr_system.entities.ListSupport;


import java.util.List;

public interface SupportMapper {
    ResponsesForSupport toDto(ListSupport listSupport);


    List<ResponsesForSupport> toDtos(List<ListSupport> responseList);
}
