package com.sam.heartbeat.model;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
public class Action {

    @Id
    private String id;
    private Integer order;
    private String title;
    private String description;
    private List<Comment> comments;
}
