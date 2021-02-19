package com.sam.heartbeat.model.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocketWrapper {

    @JsonProperty("envelope_id")
    private String envelopeId;

    private BlockMessage payload;

    private String type;

    @JsonProperty("accepts_response_payload")
    private boolean acceptsResponsePayload;

    public String toJson() {
        try {
            ObjectMapper obj = new ObjectMapper();
            return obj.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
