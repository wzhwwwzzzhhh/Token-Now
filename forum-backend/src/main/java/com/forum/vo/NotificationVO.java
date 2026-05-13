package com.forum.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationVO {
    private Long id;

    private Long userId;

    private Long fromUserId;

    private String fromUsername;

    private String fromAvatar;

    private Integer type;

    private Long targetId;

    private String content;

    private Integer isRead;

    private LocalDateTime createTime;
}
