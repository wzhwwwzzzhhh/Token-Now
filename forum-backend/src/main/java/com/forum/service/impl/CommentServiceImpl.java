package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.common.PageResult;
import com.forum.dto.CommentCreateDTO;
import com.forum.entity.Comment;
import com.forum.entity.Post;
import com.forum.entity.User;
import com.forum.mapper.CommentMapper;
import com.forum.mapper.PostMapper;
import com.forum.mapper.UserMapper;
import com.forum.service.CommentService;
import com.forum.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentVO createComment(Long userId, Long postId, CommentCreateDTO dto) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        comment.setParentId(0L);
        comment.setLikeCount(0);
        comment.setStatus(1);
        commentMapper.insert(comment);

        LambdaUpdateWrapper<Post> postWrapper = new LambdaUpdateWrapper<>();
        postWrapper.eq(Post::getId, postId);
        postWrapper.setSql("comment_count = comment_count + 1");
        postMapper.update(null, postWrapper);

        return convertToVO(comment, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此评论");
        }

        LambdaUpdateWrapper<Comment> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Comment::getId, commentId);
        wrapper.set(Comment::getStatus, 2);
        commentMapper.update(null, wrapper);

        LambdaUpdateWrapper<Post> postWrapper = new LambdaUpdateWrapper<>();
        postWrapper.eq(Post::getId, comment.getPostId());
        postWrapper.setSql("comment_count = comment_count - 1");
        postMapper.update(null, postWrapper);
    }

    @Override
    public PageResult<CommentVO> getCommentList(Long postId, Long currentUserId, Integer page, Integer size) {
        Page<Comment> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getPostId, postId);
        wrapper.eq(Comment::getStatus, 1);
        wrapper.orderByDesc(Comment::getCreateTime);
        Page<Comment> commentPage = commentMapper.selectPage(pageParam, wrapper);

        Page<CommentVO> voPage = new Page<>();
        BeanUtils.copyProperties(commentPage, voPage);
        voPage.setRecords(commentPage.getRecords().stream()
                .map(comment -> convertToVO(comment, currentUserId))
                .collect(java.util.stream.Collectors.toList()));

        return new PageResult<>(voPage.getTotal(), voPage.getRecords(), voPage.getCurrent(), voPage.getSize());
    }

    private CommentVO convertToVO(Comment comment, Long currentUserId) {
        CommentVO vo = new CommentVO();
        BeanUtils.copyProperties(comment, vo);

        User user = userMapper.selectById(comment.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
            vo.setUserAvatar(user.getAvatar());
        }

        return vo;
    }
}
