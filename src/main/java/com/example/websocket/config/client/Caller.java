package com.example.websocket.config.client;

import ch.qos.logback.core.util.TimeUtil;
import com.example.websocket.config.model.Payload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Caller {


    private String from;
    private String to;
    private String type;

    private WebSocketClient webSocketClient = new StandardWebSocketClient();
    private WebSocketSession webSocketSession;
    private ObjectMapper objectMapper =  new ObjectMapper();

    public Caller(String from, String to, String type) {
        this.from = from;
        this.to = to;
        this.type = type;
        try {
            webSocketSession = webSocketClient.doHandshake(new TextWebSocketHandler(), "ws://localhost:8080/mplayer", "").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        try {
            String request = objectMapper.writeValueAsString(Payload.builder().command("join").from(from).to(to).type(type).msg("").build());

            webSocketSession.sendMessage(new TextMessage(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        for(int i = 0; i <= 8000; i++){
            new Caller("launcher" + i, "mplayer" + i, "caller").sendMessage("");
        }


        TimeUnit.HOURS.sleep(1);

    }
}
