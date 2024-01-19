package com.example.hr_system.service.impl;

import com.example.hr_system.entities.User;
import com.example.hr_system.repository.UserRepository;
import com.example.hr_system.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean changePassword(String email, String password) {
        User user = findByEmail(email);
        if (user == null) {
            return false;
        } else {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        }
    }

    @Override
    public String changePassword(String email, String oldPassword, String password) {
        User user = findByEmail(email);
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return "successfully changed!";
        } else {
            return "password is wrong!";
        }
    }

    @Override
    public boolean checkingCode(int code) {
        User user = findByVerificationCode(code);
        if (user == null) {
            return false;
        } else {
            user.setVerificationCode(null);
            userRepository.save(user);
            return true;
        }
    }

    @Override
    public boolean sender(String email) {
        User user = findByEmail(email);
        if (user == null) {
            return false;
        } else {
            Random random = new Random();
            int min = 100000; // Minimum 6-digit number
            int max = 999999; // Maximum 6-digit number
            int code = random.nextInt(max) + min;
            user.setVerificationCode(String.valueOf(code));
            userRepository.save(user);
            return true;
        }
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new NotFoundException(
                    String.format("This email = %s not found!", email)
            );
        }
        return user.get();
    }

    @Override
    public User findByVerificationCode(int code) {
        Optional<User> user = userRepository.findByVerificationCode(String.valueOf(code));
        if (user.isEmpty()) {
            throw new NotFoundException(
                    String.format("This code = %s not found!", code)
            );
        }
        return user.get();
    }
}
