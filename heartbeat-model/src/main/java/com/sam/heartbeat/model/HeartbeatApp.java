package com.sam.heartbeat.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sam.heartbeat.model.type.Status;

@Data
@Document
@NoArgsConstructor
@RequiredArgsConstructor
public class HeartbeatApp implements Serializable {

    @Id
    private String id;
    @NonNull
    @Indexed
    private String name;
    @NonNull
    private String hostname;
    @NonNull
    private String context;
    private Status status;
//    @DBRef
//    private ParentApp parentApp;
//    private List<Incident> incidents;

    // TODO converter that takes the string from the database and returns the class type
//    private Class<T> actuatorClass;
}