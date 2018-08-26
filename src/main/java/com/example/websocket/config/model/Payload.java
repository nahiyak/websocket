package com.example.websocket.config.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {
    private String command;
    private String from;
    private String to;
    private String type;
    private String msg;
}
