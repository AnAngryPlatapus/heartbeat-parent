package com.sam.heartbeat.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    UP("Up"),
    DOWN("Down"),
    UNDER_REVIEW("Under Review"),
    MAINTENANCE("Maintenance"),
    UNDEFINED("Undefined");
    
    private String displayName;
}
