package com.sam.heartbeat.model;

import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
public class FailurePattern {

    @Id
    private String id;
    private String stackTrace;
    private Set<Action> actions;
}
