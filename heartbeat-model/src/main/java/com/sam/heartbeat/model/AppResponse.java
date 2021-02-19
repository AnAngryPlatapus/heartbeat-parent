package com.sam.heartbeat.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AppResponse {
    @NonNull
    private String logMessage;

    public static AppResponse TEST() {
        return new AppResponse("Waddup Bro");
    }
}
