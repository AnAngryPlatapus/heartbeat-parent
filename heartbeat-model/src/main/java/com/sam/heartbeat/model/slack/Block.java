package com.sam.heartbeat.model.slack;

import java.net.URI;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Block {

    @NonNull
    private BlockType type;
    @NonNull
    private Text text;
    List<Block> elements;
    private String value;
    private URI uri;

    public Block(BlockType type) {
        setType(type);
    }

    public Block(BlockType type, Text text) {
        setType(type);
        setText(text);
    }

    public static Text buildText(BlockType type, String text) {
        return new Text(type, text);
    }

    public static Text buildText(BlockType type, String text, boolean emoji) {
        return new Text(type, text, emoji);
    }

}


