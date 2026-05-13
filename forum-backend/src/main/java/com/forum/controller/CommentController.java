package com.forum.controller;

import com.forum.common.PageResult;
import com.forum.common.Result;
import com.forum.dto.CommentCreateDTO;
import com.forum.service.CommentService;
import com.forum.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Result<CommentVO> createComment(@RequestAttribute("userId") Long userId, @RequestParam Long postId, @RequestBody CommentCreateDTO dto) {
        try {
            CommentVO commentVO = commentService.createComment(userId, postId, dto);
            return Result.success("评论成功", commentVO);
        } catch (Exception e) {
            log.error("发表评论失败", e);
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteComment(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        try {
            commentService.deleteComment(userId, id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            log.error("删除评论失败", e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping
    public Result<PageResult<CommentVO>> getCommentList(
            @RequestParam Long postId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestAttribute(value = "userId", required = false) Long userId) {
        try {
            PageResult<CommentVO> result = commentService.getCommentList(postId, userId, page, size);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取评论列表失败", e);
            return Result.error(e.getMessage());
        }
    }
}
