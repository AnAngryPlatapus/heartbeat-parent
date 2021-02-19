package com.sam.heartbeat.model.slack;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RtmConnect {
    private boolean ok;
    private String url;
    private Team team;
    private Self self;
}
