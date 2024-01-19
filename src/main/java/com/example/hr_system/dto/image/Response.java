package com.example.hr_system.dto.image;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    Long id;
    String name;
    String type;
    byte[] imageData;
    Long jobSeekerId;


}
