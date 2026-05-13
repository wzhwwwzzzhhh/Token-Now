package com.forum.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentVO {
    private Long id;

    private Long postId;

    private Long userId;

    private String username;

    private String userAvatar;

    private String content;

    private Integer likeCount;

    private Boolean isLiked;

    private LocalDateTime createTime;
}
