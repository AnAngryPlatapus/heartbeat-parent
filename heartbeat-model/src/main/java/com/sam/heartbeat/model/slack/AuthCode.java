package com.sam.heartbeat.model.slack;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@RequiredArgsConstructor
public class AuthCode {

    @Id
    private String id;
    @NonNull
    private LocalDateTime createdAt;
    @NonNull
    private String code;
    private LocalDateTime requestedAt;

}
