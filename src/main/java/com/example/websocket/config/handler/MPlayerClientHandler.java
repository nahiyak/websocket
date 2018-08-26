package com.example.websocket.config.handler;

import com.example.websocket.config.model.Payload;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class MPlayerClientHandler extends TextWebSocketHandler {

    Map<String, WebSocketSession> users = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Payload payload = objectMapper.readValue(message.getPayload(), Payload.class);

        log.info("payload : {}", payload);

        String command = payload.getCommand();
        String from = payload.getFrom();
        String to = payload.getTo();
        String type = payload.getType();
        String msg = payload.getMsg();

        TextMessage response = new TextMessage(objectMapper.writeValueAsString(payload.builder().command(command).from(from).to(to).type(type).msg(msg).build()));
        TextMessage error = new TextMessage(objectMapper.writeValueAsString(payload.builder().command("error").from(from).to(to).type(type).msg("session not found").build()));

        if(StringUtils.equals(command,"join")){
            if(!users.containsKey(from))
                users.put(from, session);
            if(StringUtils.equals(type,"caller")){
                session.sendMessage(response);
            }else if(StringUtils.equals(type, "callee")){
                users.get(to).sendMessage(response);
                session.sendMessage(response);
            }else{
                session.sendMessage(error);
            }
        }else {
            if(!users.containsKey(from)) {
                session.sendMessage(error);
                return;
            }
            users.get(to).sendMessage(response);
            session.sendMessage(response);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

}
