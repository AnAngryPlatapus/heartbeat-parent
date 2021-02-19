package com.sam.heartbeat.model.slack;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionBody {
    private String payload;
    private String type;
    private Member user;
    @JsonProperty("api_app_id")
    private String apiAppId;
    private String token;
    private Text container;
    @JsonProperty("trigger_id")
    private String triggerId;
    private List<Block> actions;
}
