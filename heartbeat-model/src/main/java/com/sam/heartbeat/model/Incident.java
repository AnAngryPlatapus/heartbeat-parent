package com.sam.heartbeat.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sam.heartbeat.model.type.Effect;
import com.sam.heartbeat.model.type.Severity;

@Data
@Document
@NoArgsConstructor
public class Incident {

    @Id
    private String id;
    private Effect effect;
    private Severity severity;
    private LocalDateTime localDateTime;
    private String description;
    private List<Comment> comments;
    private String title;
    @DBRef
    private FailurePattern failurePattern;

}
