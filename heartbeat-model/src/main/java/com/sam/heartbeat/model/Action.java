package com.sam.heartbeat.model;

import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Action {
    LOGS("Recent Logs", true),
    SCRIPTS("Quick Scripts", true),
    CASE("Quick Scripts", true),
    UNDEFINED("Undefined", false);


    private final String description;
    private final boolean defaultAction;

    public static Action of(String type) {
        return Stream.of(Action.values())
                .filter(t -> t.name().equals(type))
                .findFirst()
                .orElse(Action.UNDEFINED);
    }
}
