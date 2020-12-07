package com.sam.heartbeat.model.actuator;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Link {

    @Id
    private String id;
    private String href;
    private boolean templated;
}
