package com.madikhan.profilemicro.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    private String id;
    private String content;
    private String senderUuid;
    private String senderUsername;
    private String targetUuid;
    private String targetUsername;

}
