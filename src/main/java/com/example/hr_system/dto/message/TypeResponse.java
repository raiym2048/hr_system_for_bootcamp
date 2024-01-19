package com.example.hr_system.dto.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class TypeResponse {
    private String type;
    private Object responseEntity;
}
