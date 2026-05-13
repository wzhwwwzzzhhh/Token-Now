package com.forum.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.common.PageResult;
import com.forum.vo.PostVO;

public interface PersonalService {
    PageResult<PostVO> getMyPosts(Long userId, Integer page, Integer size);

    PageResult<PostVO> getMyLikedPosts(Long userId, Integer page, Integer size);
}
