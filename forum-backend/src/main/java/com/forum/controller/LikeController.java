package com.forum.controller;

import com.forum.common.Result;
import com.forum.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    /**
     * 帖子点赞
     * @param userId
     * @param postId
     * @return
     */
    @PutMapping("/post/{postId}")
    public Result<Boolean> togglePostLike(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long postId) {
        try {
            Boolean liked = likeService.togglePostLike(userId, postId);
            return Result.success(liked ? "点赞成功" : "取消点赞", liked);
        } catch (Exception e) {
            log.error("帖子点赞操作失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     *
     * @param userId
     * @param commentId
     * @return
     */
    @PutMapping("/comment/{commentId}")
    public Result<Boolean> toggleCommentLike(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long commentId) {
        try {
            Boolean liked = likeService.toggleCommentLike(userId, commentId);
            return Result.success(liked ? "点赞成功" : "取消点赞", liked);
        } catch (Exception e) {
            log.error("评论点赞操作失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查帖子点赞状态
     * @param userId
     * @param postId
     * @return
     */
    @GetMapping("/post/{postId}/check")
    public Result<Boolean> isPostLiked(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long postId) {
        try {
            Boolean liked = likeService.isPostLiked(userId, postId);
            return Result.success(liked);
        } catch (Exception e) {
            log.error("检查帖子点赞状态失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查评论点赞状态
     * @param userId
     * @param commentId
     * @return
     */
    @GetMapping("/comment/{commentId}/check")
    public Result<Boolean> isCommentLiked(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long commentId) {
        try {
            Boolean liked = likeService.isCommentLiked(userId, commentId);
            return Result.success(liked);
        } catch (Exception e) {
            log.error("检查评论点赞状态失败", e);
            return Result.error(e.getMessage());
        }
    }
}
