package com.sam.heartbeat.model.slack;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Channel {
    private String id;
    private String name;
    @JsonProperty("is_channel")
    private boolean isChannel;
    @JsonProperty("is_group")
    private boolean isGroup;
    @JsonProperty("is_im")
    private boolean isIm;
    //TODO serialize as date
    private long created;
    private String creator;
    @JsonProperty("is_archived")
    private boolean isArchived;
    @JsonProperty("is_general")
    private boolean isGeneral;
    private int unlinked;
    @JsonProperty("name_normalized")
    private String nameNormalized;
    @JsonProperty("is_shared")
    private boolean isShared;
    @JsonProperty("is_ext_shared")
    private boolean isExtShared;
    @JsonProperty("is_org_shared")
    private boolean isOrgShared;
    @JsonProperty("pending_shared")
    private List<Object> pendingShared;
    @JsonProperty("is_pending_shared")
    private boolean isPendingShared;
    @JsonProperty("is_member")
    private boolean isMember;
    @JsonProperty("is_private")
    private boolean isPrivate;
    @JsonProperty("is_mpim")
    private boolean isMpim;
    private TopicPurpose topic;
    private TopicPurpose purpose;
    @JsonProperty("previous_names")
    private List<Object> previousNames;
    @JsonProperty("num_members")
    private int numMembers;









}
