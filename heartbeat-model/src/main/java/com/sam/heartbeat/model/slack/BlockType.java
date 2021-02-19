package com.sam.heartbeat.model.slack;

import java.util.stream.Stream;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlockType {

    ACTIONS("actions"),
    BUTTON("button"),
    DIVIDER("divider"),
    SECTION("section"),
    MARKDOWN("mrkdwn"),
    MESSAGE("message"),
    PLAINTEXT("plain_text"),
    UNDEFINED("undefined");

    @NonNull
    @JsonValue
    private final String displayName;

    public static BlockType of(String type) {
        return Stream.of(BlockType.values())
                .filter(t -> t.getDisplayName().equals(type))
                .findFirst()
                .orElse(BlockType.UNDEFINED);
    }
}

