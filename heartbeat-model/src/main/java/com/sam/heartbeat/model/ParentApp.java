package com.sam.heartbeat.model;

import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class ParentApp {
    @Id
    private String id;
    @DBRef
    private List<HeartbeatApp> microServices;
    private String name;
    private String description;
}
