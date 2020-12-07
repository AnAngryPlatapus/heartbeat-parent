package com.sam.heartbeat.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Severity {

    FATAL("Fatal", 2),
    MAJOR("Serious", 3),
    MINOR("Minor", 4),
    RESOLVED("Resolved", 5),
    UNDEFINED("Undefined", 0);

    private String description;
    private int rank;

}
