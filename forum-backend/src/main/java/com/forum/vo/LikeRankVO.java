package com.forum.vo;

import lombok.Data;

@Data
public class LikeRankVO {
    private Long userId;

    private String username;

    private String avatar;

    private Long likeCount;

    private Integer rank;
}
