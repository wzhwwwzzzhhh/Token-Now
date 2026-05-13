package com.forum.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;

    private String username;

    private String avatar;

    private String bio;

    private Integer followCount;

    private Integer fansCount;

    private Boolean isFollowing;

    private LocalDateTime createTime;

    private String token;
}
