package com.example.hr_system.controller;

import com.example.hr_system.dto.vacancy.VacancyResponseForPopularCategories;
import com.example.hr_system.service.VacancyService;
import lombok.AllArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/main")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class MainScreenController {
    private final VacancyService vacancyService;

    @GetMapping("/get/list/popular/position")
    public List<VacancyResponseForPopularCategories> getPopularPositions(){
        return vacancyService.getPopularPosition();
    }
}
