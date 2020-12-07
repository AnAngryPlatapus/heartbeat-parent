package com.sam.heartbeat.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Pulse implements Serializable {

    @Id
    private String id;
    @NonNull
    private Object actuatorInfo;
    @NonNull
    private LocalDateTime pingDate;
    @NonNull
    private Long requestDuration;
    @DBRef
    @NonNull
    private HeartbeatApp heartbeatApp;

    public static Pulse DEFAULT(HeartbeatApp heartbeatApp) {
        return new Pulse(new HashMap<>(), LocalDateTime.now(), Long.MAX_VALUE, heartbeatApp);
    }

}
