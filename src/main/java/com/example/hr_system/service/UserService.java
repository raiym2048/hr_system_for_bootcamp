package com.example.hr_system.service;

import com.example.hr_system.entities.User;

public interface UserService {
    boolean checkingCode(int code);

    boolean sender(String email);

    User findByEmail(String email);

    boolean changePassword(String email, String password);

    String changePassword(String email, String oldPassword, String newPassword);

    User findByVerificationCode(int code);
}
