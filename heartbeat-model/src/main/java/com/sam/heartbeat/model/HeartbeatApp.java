package com.sam.heartbeat.model;

import static java.lang.String.format;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.slack.api.model.block.HeaderBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.model.slack.Channel;
import com.sam.heartbeat.model.slack.Member;
import com.sam.heartbeat.model.type.Status;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
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
    @NonNull
    private String logPath;

    //TODO might make configuration properties an embedded document
    @NonNull
    private String channelName;
    private String channelId;
    @NonNull
    private String botName;
    private String botId;
    public boolean slackConfigured = Boolean.FALSE;

    public Mono<HeartbeatApp> configureSlack(Channel channel, Member member) {
        setChannelId(channel.getId());
        setBotId(member.getId());
        setSlackConfigured(true);
        return Mono.just(this);
    }

    @JsonIgnore
    public Mono<List<LayoutBlock>> logsBlock(Mono<String> formattedLogs) {
        return formattedLogs.map(logs -> List.of(
                HeaderBlock.builder()
                        .text(PlainTextObject.builder()
                                .text(format("Logs for %s", getName()))
                                .emoji(true)
                                .build())
                        .build(),
                SectionBlock.builder()
                        .text(MarkdownTextObject.builder()
                                .text(format("Check %s for full logs", getLogPath()))
                                .build())
                        .build(),
                SectionBlock.builder()
                        .text(MarkdownTextObject.builder()
                                .text(logs)
                                .build())
                        .build()));
    }
}