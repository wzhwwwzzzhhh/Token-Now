package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.common.PageResult;
import com.forum.dto.PostCreateDTO;
import com.forum.dto.PostUpdateDTO;
import com.forum.entity.Post;
import com.forum.entity.User;
import com.forum.mapper.PostMapper;
import com.forum.mapper.UserMapper;
import com.forum.service.PostService;
import com.forum.vo.PostVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostVO createPost(Long userId, PostCreateDTO dto) {
        Post post = new Post();
        post.setUserId(userId);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setViewCount(0);
        post.setStatus(1);
        postMapper.insert(post);

        return convertToVO(post, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostVO updatePost(Long userId, Long postId, PostUpdateDTO dto) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("无权修改此帖子");
        }

        LambdaUpdateWrapper<Post> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Post::getId, postId);
        wrapper.set(Post::getTitle, dto.getTitle());
        wrapper.set(Post::getContent, dto.getContent());
        postMapper.update(null, wrapper);

        return convertToVO(postMapper.selectById(postId), userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(Long userId, Long postId) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此帖子");
        }

        LambdaUpdateWrapper<Post> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Post::getId, postId);
        wrapper.set(Post::getStatus, 2);
        postMapper.update(null, wrapper);
    }

    @Override
    public PostVO getPostById(Long postId, Long currentUserId) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }

        LambdaUpdateWrapper<Post> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Post::getId, postId);
        wrapper.setSql("view_count = view_count + 1");
        postMapper.update(null, wrapper);

        return convertToVO(postMapper.selectById(postId), currentUserId);
    }

    @Override
    public PageResult<PostVO> getPostList(Long currentUserId, Integer page, Integer size) {
        Page<Post> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getStatus, 1);
        wrapper.orderByDesc(Post::getCreateTime);
        Page<Post> postPage = postMapper.selectPage(pageParam, wrapper);

        Page<PostVO> voPage = new Page<>();
        BeanUtils.copyProperties(postPage, voPage);
        voPage.setRecords(postPage.getRecords().stream()
                .map(post -> convertToVO(post, currentUserId))
                .collect(java.util.stream.Collectors.toList()));

        return new PageResult<>(voPage.getTotal(), voPage.getRecords(), voPage.getCurrent(), voPage.getSize());
    }

    @Override
    public PageResult<PostVO> searchPosts(Long currentUserId, String keyword, Integer page, Integer size) {
        Page<Post> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getStatus, 1);
        wrapper.like(Post::getTitle, keyword);
        wrapper.orderByDesc(Post::getCreateTime);
        Page<Post> postPage = postMapper.selectPage(pageParam, wrapper);

        Page<PostVO> voPage = new Page<>();
        BeanUtils.copyProperties(postPage, voPage);
        voPage.setRecords(postPage.getRecords().stream()
                .map(post -> convertToVO(post, currentUserId))
                .collect(java.util.stream.Collectors.toList()));

        return new PageResult<>(voPage.getTotal(), voPage.getRecords(), voPage.getCurrent(), voPage.getSize());
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
