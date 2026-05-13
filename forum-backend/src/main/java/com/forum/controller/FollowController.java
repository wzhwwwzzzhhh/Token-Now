package com.forum.controller;

import com.forum.common.PageResult;
import com.forum.common.Result;
import com.forum.service.FollowService;
import com.forum.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/{targetUserId}")
    public Result<Void> followUser(@RequestAttribute("userId") Long userId, @PathVariable Long targetUserId) {
        try {
            followService.followUser(userId, targetUserId);
            return Result.success();
        } catch (Exception e) {
            log.error("关注用户失败", e);
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{targetUserId}")
    public Result<Void> unfollowUser(@RequestAttribute("userId") Long userId, @PathVariable Long targetUserId) {
        try {
            followService.unfollowUser(userId, targetUserId);
            return Result.success();
        } catch (Exception e) {
            log.error("取消关注失败", e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/check/{targetUserId}")
    public Result<Boolean> isFollowing(@RequestAttribute("userId") Long userId, @PathVariable Long targetUserId) {
        try {
            Boolean isFollowing = followService.isFollowing(userId, targetUserId);
            return Result.success(isFollowing);
        } catch (Exception e) {
            log.error("检查关注状态失败", e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/following")
    public Result<PageResult<UserVO>> getFollowingList(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            PageResult<UserVO> result = followService.getFollowingList(userId, page, size);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取关注列表失败", e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/followers")
    public Result<PageResult<UserVO>> getFollowersList(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            PageResult<UserVO> result = followService.getFollowersList(userId, page, size);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取粉丝列表失败", e);
            return Result.error(e.getMessage());
        }
    }
}
