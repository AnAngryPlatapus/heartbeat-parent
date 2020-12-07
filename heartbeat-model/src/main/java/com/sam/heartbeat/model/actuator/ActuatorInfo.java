package com.sam.heartbeat.model.actuator;

import java.util.Map;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class ActuatorInfo {

    private Map<String, Link> _links;
}
