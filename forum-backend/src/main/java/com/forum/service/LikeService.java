package com.forum.service;

public interface LikeService {
    Boolean togglePostLike(Long userId, Long postId);

    Boolean toggleCommentLike(Long userId, Long commentId);

    Boolean isPostLiked(Long userId, Long postId);

    Boolean isCommentLiked(Long userId, Long commentId);
}
