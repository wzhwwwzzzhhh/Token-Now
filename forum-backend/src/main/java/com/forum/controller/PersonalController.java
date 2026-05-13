package com.forum.controller;

import com.forum.common.PageResult;
import com.forum.common.Result;
import com.forum.service.PersonalService;
import com.forum.vo.PostVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/personal")
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    @GetMapping("/posts")
    public Result<PageResult<PostVO>> getMyPosts(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            PageResult<PostVO> result = personalService.getMyPosts(userId, page, size);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取我的帖子列表失败", e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/liked-posts")
    public Result<PageResult<PostVO>> getMyLikedPosts(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            PageResult<PostVO> result = personalService.getMyLikedPosts(userId, page, size);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取我的点赞记录失败", e);
            return Result.error(e.getMessage());
        }
    }
}
