package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.common.PageResult;
import com.forum.entity.Post;
import com.forum.entity.User;
import com.forum.mapper.PostMapper;
import com.forum.mapper.UserMapper;
import com.forum.service.RankingService;
import com.forum.vo.LikeRankVO;
import com.forum.vo.PostRankVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RankingServiceImpl implements RankingService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public PageResult<PostRankVO> getPostRanking(String type, Integer page, Integer size) {
        Page<Post> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getStatus, 1);

        if ("week".equals(type)) {
            LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
            wrapper.ge(Post::getCreateTime, weekAgo);
        }

        wrapper.orderByDesc(Post::getLikeCount);
        wrapper.orderByDesc(Post::getCreateTime);
        Page<Post> postPage = postMapper.selectPage(pageParam, wrapper);

        List<PostRankVO> records = postPage.getRecords().stream()
                .map(post -> {
                    PostRankVO vo = new PostRankVO();
                    BeanUtils.copyProperties(post, vo);

                    User user = userMapper.selectById(post.getUserId());
                    if (user != null) {
                        vo.setUsername(user.getUsername());
                        vo.setUserAvatar(user.getAvatar());
                    }

                    return vo;
                })
                .collect(Collectors.toList());

        for (int i = 0; i < records.size(); i++) {
            records.get(i).setRank((int) ((page - 1) * size + i + 1));
        }

        return new PageResult<>(postPage.getTotal(), records, postPage.getCurrent(), postPage.getSize());
    }

    @Override
    public List<LikeRankVO> getLikeRanking(String period, Integer limit) {
        String rankKey = "like:rank:" + period;
        Set<String> userIds = redisTemplate.opsForZSet()
                .reverseRangeByScore(rankKey, 0, Double.MAX_VALUE, 0, limit);

        List<LikeRankVO> list = new ArrayList<>();
        int rank = 1;
        for (String userIdStr : userIds) {
            Long userId = Long.valueOf(userIdStr);
            Double score = redisTemplate.opsForZSet().score(rankKey, userIdStr);

            User user = userMapper.selectById(userId);
            if (user == null) continue;

            LikeRankVO vo = new LikeRankVO();
            vo.setUserId(userId);
            vo.setUsername(user.getUsername());
            vo.setAvatar(user.getAvatar());
            vo.setLikeCount(score != null ? score.longValue() : 0);
            vo.setRank(rank++);
            list.add(vo);
        }

        return list;
    }
}
