package com.example.hr_system.service;

import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.entities.FileData;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileDataService {
    FileData uploadFile(MultipartFile file, FileData pdf) ;

    FileData uploadFile(MultipartFile file) ;

    void downloadFile(Long id, HttpServletResponse http) ;

    void getFileById(Long id, HttpServletResponse httpServletResponse);
}
