package com.sam.heartbeat.model.slack;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import com.sam.heartbeat.model.HeartbeatApp;

@Data
public class SocketMessage {

    private String type;
    private String channel;
    private String user;
    private String text;
    private String ts;

    public SocketMessage(HeartbeatApp app) {
        setType("message");
        setText("");
        setText(String.format("%s is %s", app.getName(), app.getStatus().getDisplayName()));
        setTs(Long.toString(LocalDateTime.now().toEpochSecond(OffsetDateTime.now().getOffset())));
        setUser(app.getBotId());
        setChannel(app.getChannelId());
    }

    public String toJson() {
        try {
            ObjectMapper obj = new ObjectMapper();
            return obj.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
