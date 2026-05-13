package com.forum.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostVO {
    private Long id;

    private String title;

    private String content;

    private Long userId;

    private String username;

    private String userAvatar;

    private Integer likeCount;

    private Integer commentCount;

    private Integer viewCount;

    private Boolean isLiked;

    private Boolean isFollowing;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
