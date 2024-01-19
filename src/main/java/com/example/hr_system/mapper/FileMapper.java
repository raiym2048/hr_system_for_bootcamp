package com.example.hr_system.mapper;

import com.example.hr_system.dto.file.FileResponse;
import com.example.hr_system.entities.FileData;

import java.util.List;

public interface FileMapper {
    FileResponse toDto(FileData fileData);

    List<FileResponse> toDtos(List<FileData> files);
}
