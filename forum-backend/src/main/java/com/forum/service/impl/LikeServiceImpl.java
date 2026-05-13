package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.forum.entity.Comment;
import com.forum.entity.Like;
import com.forum.entity.Post;
import com.forum.mapper.CommentMapper;
import com.forum.mapper.LikeMapper;
import com.forum.mapper.PostMapper;
import com.forum.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean togglePostLike(Long userId, Long postId) {
        String likeKey = "likes:post:" + postId;
        String member = userId.toString();

        Double score = redisTemplate.opsForZSet().score(likeKey, member);
        boolean liked = score != null;

        if (liked) {
            redisTemplate.opsForZSet().remove(likeKey, member);
            redisTemplate.opsForZSet().incrementScore("like:rank:daily", member, -1);
            redisTemplate.opsForZSet().incrementScore("like:rank:weekly", member, -1);
            redisTemplate.opsForZSet().incrementScore("like:rank:monthly", member, -1);
            redisTemplate.opsForZSet().incrementScore("like:rank:all", member, -1);

            likeMapper.delete(new LambdaQueryWrapper<Like>()
                    .eq(Like::getUserId, userId)
                    .eq(Like::getTargetType, 1)
                    .eq(Like::getTargetId, postId));

            postMapper.update(null, new LambdaUpdateWrapper<Post>()
                    .eq(Post::getId, postId).setSql("like_count = like_count - 1"));

            log.info("取消点赞帖子: userId={}, postId={}", userId, postId);
            return false;
        } else {
            redisTemplate.opsForZSet().add(likeKey, member, System.currentTimeMillis());
            redisTemplate.opsForZSet().incrementScore("like:rank:daily", member, 1);
            redisTemplate.opsForZSet().incrementScore("like:rank:weekly", member, 1);
            redisTemplate.opsForZSet().incrementScore("like:rank:monthly", member, 1);
            redisTemplate.opsForZSet().incrementScore("like:rank:all", member, 1);

            Like like = new Like();
            like.setUserId(userId);
            like.setTargetType(1);
            like.setTargetId(postId);
            likeMapper.insert(like);

            postMapper.update(null, new LambdaUpdateWrapper<Post>()
                    .eq(Post::getId, postId).setSql("like_count = like_count + 1"));

            log.info("点赞帖子: userId={}, postId={}", userId, postId);
            return true;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean toggleCommentLike(Long userId, Long commentId) {
        String likeKey = "likes:comment:" + commentId;
        String member = userId.toString();

        Double score = redisTemplate.opsForZSet().score(likeKey, member);
        boolean liked = score != null;

        if (liked) {
            redisTemplate.opsForZSet().remove(likeKey, member);

            likeMapper.delete(new LambdaQueryWrapper<Like>()
                    .eq(Like::getUserId, userId)
                    .eq(Like::getTargetType, 2)
                    .eq(Like::getTargetId, commentId));

            commentMapper.update(null, new LambdaUpdateWrapper<Comment>()
                    .eq(Comment::getId, commentId).setSql("like_count = like_count - 1"));

            log.info("取消点赞评论: userId={}, commentId={}", userId, commentId);
            return false;
        } else {
            redisTemplate.opsForZSet().add(likeKey, member, System.currentTimeMillis());

            Like like = new Like();
            like.setUserId(userId);
            like.setTargetType(2);
            like.setTargetId(commentId);
            likeMapper.insert(like);

            commentMapper.update(null, new LambdaUpdateWrapper<Comment>()
                    .eq(Comment::getId, commentId).setSql("like_count = like_count + 1"));

            log.info("点赞评论: userId={}, commentId={}", userId, commentId);
            return true;
        }
    }

    @Override
    public Boolean isPostLiked(Long userId, Long postId) {
        String likeKey = "likes:post:" + postId;
        Double score = redisTemplate.opsForZSet().score(likeKey, userId.toString());
        return score != null;
    }

    @Override
    public Boolean isCommentLiked(Long userId, Long commentId) {
        String likeKey = "likes:comment:" + commentId;
        Double score = redisTemplate.opsForZSet().score(likeKey, userId.toString());
        return score != null;
    }
}
