package com.sam.heartbeat.model.slack;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Conversation {
    private boolean ok;
    private List<Channel> channels;

    @JsonProperty("response_metadata")
    private Metadata responseMetadata;
}
