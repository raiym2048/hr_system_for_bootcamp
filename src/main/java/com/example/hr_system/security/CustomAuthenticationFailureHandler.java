package com.example.hr_system.security;

import com.example.hr_system.exception.BlockedException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Throwable rootCause = exception.getCause();
        if (rootCause instanceof BlockedException) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("THIS USER IS BLOCKED");
        }
    }
}
