package com.example.hr_system.service.emailSender;

import com.example.hr_system.entities.FileData;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.example.hr_system.exception.NotFoundException;
import com.example.hr_system.repository.FileRepository;
import com.example.hr_system.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.Objects;

@Service
@AllArgsConstructor
public class EmailSenderService {

    private JavaMailSender mailSender;
    private UserRepository userRepository;
    private FileRepository fileRepository;

    public void inviteJobSeeker(Long jobId ,Long empId, String message){
        String userEmail = userRepository.findById(empId).orElseThrow(() ->
                new NotFoundException("userId not found!", HttpStatus.NOT_FOUND)).getEmail();
        String jobSeekerEmail = userRepository.findById(jobId).orElseThrow(() ->
                new NotFoundException("userId not found!",HttpStatus.NOT_FOUND)).getEmail();
        SimpleMailMessage inviteMessage = new SimpleMailMessage();
        inviteMessage.setFrom(userEmail);
        inviteMessage.setTo(jobSeekerEmail);
        inviteMessage.setText(message);
        mailSender.send(inviteMessage);
    }
    public void sendEmail(String toEmail, String subject, String body, int code){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("devsfactoryBack@gmail.com");
        message.setTo(toEmail);
        message.setText(body+"\n The refresh code: "+ code);
        message.setSubject(subject);
        mailSender.send(message);
    }

    public void inviteJobSeeker(Long empId, Long jobId, String message, Long fileid) throws MessagingException {

        String userEmail = userRepository.findById(empId).orElseThrow(() ->
                new NotFoundException("userId not found!",HttpStatus.NOT_FOUND)).getEmail();
        String jobSeekerEmail = userRepository.findById(jobId).orElseThrow(() ->
                new NotFoundException("userId not found!",HttpStatus.NOT_FOUND)).getEmail();
        FileData fileData = fileRepository.findById(fileid).orElseThrow(() ->
                new NotFoundException("userId not found!",HttpStatus.NOT_FOUND));
        MimeMessage messages = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(messages, true);
        helper.setFrom("devsfactoryback@gmail.com");
        helper.setTo(jobSeekerEmail);
        helper.setText(userEmail+" is sent you message: "+message);

        Resource resource = null;

        try {
            resource = new UrlResource(fileData.getPath());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        helper.addAttachment(Objects.requireNonNull(fileData.getName()), resource, "application/pdf");
//        for(Resource attachment: fileData)
//            helper.addAttachment(Objects.requireNonNull(attachment.getFilename()), attachment, "application/pdf");

        mailSender.send(messages);
    }
}