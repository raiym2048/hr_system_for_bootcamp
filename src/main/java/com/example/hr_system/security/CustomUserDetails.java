package com.example.hr_system.security;

import com.example.hr_system.entities.User;
import com.example.hr_system.exception.BlockedException;
import com.example.hr_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetails implements UserDetailsService {
    private final UserRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> auth = authRepository.findByEmail(email);

        if (auth.isEmpty()) {
            throw new UsernameNotFoundException("User '" + email + "' not found");
        }
        User user = auth.get();
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().toString())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }


}
