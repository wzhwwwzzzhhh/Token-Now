package com.forum.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostRankVO {
    private Long id;

    private String title;

    private Long userId;

    private String username;

    private String userAvatar;

    private Integer likeCount;

    private Integer commentCount;

    private Integer viewCount;

    private LocalDateTime createTime;

    private Integer rank;
}
