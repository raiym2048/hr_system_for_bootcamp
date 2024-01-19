package com.example.hr_system.config;

import com.example.hr_system.entities.User;
import com.example.hr_system.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Collectors;
@AllArgsConstructor
public class MyHandshakeInterceptor implements HandshakeInterceptor {
private ChatService chatService;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws IOException {
        System.out.println("\n\nstarted!");

        // Если токен передается через параметры запроса
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            // Получение токена из параметра запроса
            String tokenParam = servletRequest.getServletRequest().getParameter("Authorization");


            System.out.println("the token: "+tokenParam);

            if (tokenParam != null && tokenParam.startsWith("Bearer ")) {

                attributes.put("token", tokenParam); // Добавление токена в атрибуты сессии
                System.out.println("Token received from query param: " + tokenParam);


                // Получение пользователя из токена
                User user = chatService.getUsernameFromToken(tokenParam);
                System.out.println(user.getEmail());
                attributes.put("sender", user.getEmail());
                System.out.println("\n\nthe email:"+user.getEmail());
            } else {
                System.out.println("Invalid or missing Authorization parameter in query");
            }
        }


        return true;
    }



    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        System.out.println("\n\nafterHandshake");

        // Постобработка рукопожатия. Обычно здесь ничего делать не нужно.
    }
}
