package com.example.hr_system.dto.file;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {

    Long id;
    String name;
    String type;
    byte[] fileData;
    Long jobSeekerId;
    String path;


}
