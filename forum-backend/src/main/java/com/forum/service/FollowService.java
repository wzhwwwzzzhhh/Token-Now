package com.forum.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.common.PageResult;
import com.forum.vo.UserVO;

public interface FollowService {
    void followUser(Long userId, Long targetUserId);

    void unfollowUser(Long userId, Long targetUserId);

    Boolean isFollowing(Long userId, Long targetUserId);

    PageResult<UserVO> getFollowingList(Long userId, Integer page, Integer size);

    PageResult<UserVO> getFollowersList(Long userId, Integer page, Integer size);
}
