package com.forum.controller;

import com.forum.common.PageResult;
import com.forum.common.Result;
import com.forum.service.RankingService;
import com.forum.vo.LikeRankVO;
import com.forum.vo.PostRankVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rankings")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping("/posts")
    public Result<PageResult<PostRankVO>> getPostRanking(
            @RequestParam(defaultValue = "all") String type,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            PageResult<PostRankVO> result = rankingService.getPostRanking(type, page, size);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取帖子排行榜失败", e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/like")
    public Result<List<LikeRankVO>> getLikeRanking(
            @RequestParam(defaultValue = "weekly") String period,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<LikeRankVO> result = rankingService.getLikeRanking(period, limit);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取点赞排行榜失败", e);
            return Result.error(e.getMessage());
        }
    }
}
