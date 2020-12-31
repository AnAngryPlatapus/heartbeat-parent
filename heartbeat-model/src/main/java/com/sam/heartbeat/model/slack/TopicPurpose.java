package com.sam.heartbeat.model.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TopicPurpose {
    private String value;
    private String creator;
    @JsonProperty("last_set")
    private int lastSet;
}
