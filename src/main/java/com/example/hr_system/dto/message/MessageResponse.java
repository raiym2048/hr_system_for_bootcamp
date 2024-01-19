package com.example.hr_system.dto.message;

import com.example.hr_system.dto.file.FileResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MessageResponse {
    private Long messageId;

    private Boolean amIAuthor;
    private String sender;
    private String recipient;
    private Boolean read;
    private Long scribeId;
    private String sentTime;
    private String content;
    private List<FileResponse> fileResponse;

}
