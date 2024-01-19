package com.example.hr_system.controller;


import com.example.hr_system.service.EmployerService;
import com.example.hr_system.service.FileDataService;

import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class FileController {

    private final FileDataService fileDataService;
    private final EmployerService employerService;


    @GetMapping("/resume/{id}")
    public void getFileById(@PathVariable Long id, HttpServletResponse httpServletResponse){
        fileDataService.getFileById(id, httpServletResponse);
    }

    @GetMapping("/download/file/{id}")
    public void downloadFile(@PathVariable Long id, HttpServletResponse http) throws IOException {
        fileDataService.downloadFile(id, http);
    }





    @PostMapping("resume/upload/{employerId}")
    public ResponseEntity<?> uploadResume1(@RequestPart MultipartFile file, @PathVariable Long employerId) throws IOException {

        return ResponseEntity.status(HttpStatus.OK)
                .body(employerService.uploadResume(file, employerId));
    }
    @PostMapping("image/upload/{employerId}")
    public ResponseEntity<?> uploadImage1(@RequestPart MultipartFile file, @PathVariable Long employerId) throws IOException {

        return ResponseEntity.status(HttpStatus.OK)
                .body(employerService.uploadImage(file, employerId));
    }

    @PostMapping("chat/upload/{userId}")
    public ResponseEntity<?> uploadFileChat(@RequestPart MultipartFile file, @PathVariable Long userId) throws IOException {

        return ResponseEntity.status(HttpStatus.OK)
                .body(employerService.uploadFileChat(file, userId));
    }

}
