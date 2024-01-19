package com.example.hr_system.config;

import com.example.hr_system.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatService chatService;



    @Bean
    public WebSocketHandler myHandler() {
        return new MyHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/ws", "/wss")
                .setAllowedOrigins("*")
                .addInterceptors(new MyHandshakeInterceptor(chatService));
    }
    @Bean
    public WebSocketHandler getWsEndpoint() {
        return new WsEndpoint();
    }
}



