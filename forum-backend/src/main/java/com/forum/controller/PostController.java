package com.forum.controller;

import com.forum.common.PageResult;
import com.forum.common.Result;
import com.forum.dto.PostCreateDTO;
import com.forum.dto.PostUpdateDTO;
import com.forum.service.PostService;
import com.forum.vo.PostVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public Result<PostVO> createPost(@RequestAttribute("userId") Long userId, @RequestBody PostCreateDTO dto) {
        try {
            PostVO postVO = postService.createPost(userId, dto);
            return Result.success("发布成功", postVO);
        } catch (Exception e) {
            log.error("发布帖子失败", e);
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<PostVO> updatePost(@RequestAttribute("userId") Long userId, @PathVariable Long id, @RequestBody PostUpdateDTO dto) {
        try {
            PostVO postVO = postService.updatePost(userId, id, dto);
            return Result.success("更新成功", postVO);
        } catch (Exception e) {
            log.error("更新帖子失败", e);
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> deletePost(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        try {
            postService.deletePost(userId, id);
            return Result.success();
        } catch (Exception e) {
            log.error("删除帖子失败", e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<PostVO> getPostById(@PathVariable Long id, @RequestAttribute(value = "userId", required = false) Long userId) {
        try {
            PostVO postVO = postService.getPostById(id, userId);
            return Result.success(postVO);
        } catch (Exception e) {
            log.error("获取帖子详情失败", e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping
    public Result<PageResult<PostVO>> getPostList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestAttribute(value = "userId", required = false) Long userId) {
        try {
            PageResult<PostVO> result = postService.getPostList(userId, page, size);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取帖子列表失败", e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/search")
    public Result<PageResult<PostVO>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestAttribute(value = "userId", required = false) Long userId) {
        try {
            PageResult<PostVO> result = postService.searchPosts(userId, keyword, page, size);
            return Result.success(result);
        } catch (Exception e) {
            log.error("搜索帖子失败", e);
            return Result.error(e.getMessage());
        }
    }
}
