package com.sam.heartbeat.model.slack;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Users {
    private boolean ok;
    private List<Member> members;

    @JsonProperty("response_metadata")
    private Metadata responseMetadata;
}
