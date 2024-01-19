package com.example.hr_system.config;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class WsEndpoint extends TextWebSocketHandler {

    public WsEndpoint(){}

    public WebSocketMessage<String> wsMsg;

    // This method will be called after successful websocket connection.
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws java.lang.Exception {

        wsMsg = new TextMessage(new String("Connection ok!"));
        session.sendMessage(wsMsg);
    }

    // This method is called if message was recieved.
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {

        wsMsg = new TextMessage(new String("Message recieved!"));
        session.sendMessage(wsMsg);
    }
}