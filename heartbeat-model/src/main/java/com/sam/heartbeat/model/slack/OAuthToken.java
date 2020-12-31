package com.sam.heartbeat.model.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class OAuthToken {
    @JsonProperty("access_token")
    private String accessToken;
    private String scope;
    private String createdAt;
}