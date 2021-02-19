package com.sam.heartbeat.model.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member {
    private String id;
    @JsonProperty("team_id")
    private String teamId;
    private String name;
    @JsonProperty("real_name")
    private String realName;
    private String username;
}
