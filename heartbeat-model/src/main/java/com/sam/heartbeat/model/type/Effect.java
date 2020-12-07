package com.sam.heartbeat.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Effect {

    OUTAGE("Outage", Severity.FATAL),
    PARTIAL_OUTAGE("Partial Outage", Severity.MAJOR),
    MINOR_PERFORMANCE_DEGRADATION("Minor Performance Degradation", Severity.MAJOR),
    MAJOR_PERFORMANCE_DEGRADATION("Major Performance Degradation", Severity.FATAL),
    UNHANDLED_ERROR("Singular Unhandled Error", Severity.MINOR),
    REPEATED_UNHANDLED_ERROR("Repeated Unhanded Error", Severity.MAJOR),
    UNDEFINED("Undefined", Severity.UNDEFINED);

    private String description;
    private Severity severity;

}
