package com.sam.heartbeat.model.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Metadata {
    @JsonProperty("next_cursor")
    private String nextCursor;
}
