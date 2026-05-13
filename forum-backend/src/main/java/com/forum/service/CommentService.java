package com.forum.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.common.PageResult;
import com.forum.dto.CommentCreateDTO;
import com.forum.vo.CommentVO;

public interface CommentService {
    CommentVO createComment(Long userId, Long postId, CommentCreateDTO dto);

    void deleteComment(Long userId, Long commentId);

    PageResult<CommentVO> getCommentList(Long postId, Long currentUserId, Integer page, Integer size);
}
