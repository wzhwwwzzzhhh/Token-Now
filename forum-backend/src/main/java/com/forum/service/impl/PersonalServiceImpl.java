package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.common.PageResult;
import com.forum.entity.Like;
import com.forum.entity.Post;
import com.forum.entity.User;
import com.forum.mapper.LikeMapper;
import com.forum.mapper.PostMapper;
import com.forum.mapper.UserMapper;
import com.forum.service.PersonalService;
import com.forum.vo.PostVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonalServiceImpl implements PersonalService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult<PostVO> getMyPosts(Long userId, Integer page, Integer size) {
        Page<Post> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getUserId, userId);
        wrapper.eq(Post::getStatus, 1);
        wrapper.orderByDesc(Post::getCreateTime);
        Page<Post> postPage = postMapper.selectPage(pageParam, wrapper);

        List<PostVO> records = postPage.getRecords().stream()
                .map(post -> convertToVO(post, userId))
                .collect(Collectors.toList());

        return new PageResult<>(postPage.getTotal(), records, postPage.getCurrent(), postPage.getSize());
    }

    @Override
    public PageResult<PostVO> getMyLikedPosts(Long userId, Integer page, Integer size) {
        Page<Like> likePageParam = new Page<>(page, size);
        LambdaQueryWrapper<Like> likeWrapper = new LambdaQueryWrapper<>();
        likeWrapper.eq(Like::getUserId, userId);
        likeWrapper.eq(Like::getTargetType, 1);
        likeWrapper.orderByDesc(Like::getCreateTime);
        Page<Like> likePage = likeMapper.selectPage(likePageParam, likeWrapper);

        List<PostVO> records = likePage.getRecords().stream()
                .map(like -> {
                    Post post = postMapper.selectById(like.getTargetId());
                    return convertToVO(post, userId);
                })
                .collect(Collectors.toList());

        return new PageResult<>(likePage.getTotal(), records, likePage.getCurrent(), likePage.getSize());
    }

    private PostVO convertToVO(Post post, Long currentUserId) {
        PostVO vo = new PostVO();
        BeanUtils.copyProperties(post, vo);

        User user = userMapper.selectById(post.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
            vo.setUserAvatar(user.getAvatar());
        }

        return vo;
    }
}
