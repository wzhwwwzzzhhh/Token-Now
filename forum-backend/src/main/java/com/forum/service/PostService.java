package com.forum.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.common.PageResult;
import com.forum.dto.PostCreateDTO;
import com.forum.dto.PostUpdateDTO;
import com.forum.vo.PostVO;

public interface PostService {
    PostVO createPost(Long userId, PostCreateDTO dto);

    PostVO updatePost(Long userId, Long postId, PostUpdateDTO dto);

    void deletePost(Long userId, Long postId);

    PostVO getPostById(Long postId, Long currentUserId);

    PageResult<PostVO> getPostList(Long currentUserId, Integer page, Integer size);

    PageResult<PostVO> searchPosts(Long currentUserId, String keyword, Integer page, Integer size);
}
