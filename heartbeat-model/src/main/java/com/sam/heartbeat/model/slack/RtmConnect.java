package com.sam.heartbeat.model.slack;

import lombok.Data;

@Data
public class RtmConnect {
    private boolean ok;
    private String url;
    private Team team;
    private Self self;
}
