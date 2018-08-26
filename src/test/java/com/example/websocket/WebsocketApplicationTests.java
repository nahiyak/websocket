package com.example.websocket;

import com.example.websocket.config.model.Payload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebsocketApplicationTests {


    @Test
    public void contextLoads() {
    }

    @Test
    public void websocketTest() throws ExecutionException, InterruptedException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(Payload.builder().command("join").from("launcher1").to("mplayer1").type("caller").msg("").build());
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketSession webSocketSession = webSocketClient.doHandshake(new TextWebSocketHandler(), "ws://localhost:8080/mplayer","").get();
        webSocketSession.sendMessage(new TextMessage(request));
    }
}
