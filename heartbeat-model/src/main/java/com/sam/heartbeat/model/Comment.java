package com.sam.heartbeat.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
public class Comment {

    @Id
    private String id;
    private String user;
    private String content;

}
