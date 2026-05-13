package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.common.PageResult;
import com.forum.entity.Follow;
import com.forum.entity.User;
import com.forum.mapper.FollowMapper;
import com.forum.mapper.UserMapper;
import com.forum.service.FollowService;
import com.forum.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void followUser(Long userId, Long targetUserId) {
        if (userId.equals(targetUserId)) {
            throw new RuntimeException("不能关注自己");
        }

        User targetUser = userMapper.selectById(targetUserId);
        if (targetUser == null) {
            throw new RuntimeException("目标用户不存在");
        }

        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getUserId, userId);
        wrapper.eq(Follow::getFollowUserId, targetUserId);
        Follow existFollow = followMapper.selectOne(wrapper);
        if (existFollow != null) {
            throw new RuntimeException("已经关注过了");
        }

        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowUserId(targetUserId);
        followMapper.insert(follow);

        LambdaUpdateWrapper<User> currentUserWrapper = new LambdaUpdateWrapper<>();
        currentUserWrapper.eq(User::getId, userId);
        currentUserWrapper.setSql("follow_count = follow_count + 1");
        userMapper.update(null, currentUserWrapper);

        LambdaUpdateWrapper<User> targetUserWrapper = new LambdaUpdateWrapper<>();
        targetUserWrapper.eq(User::getId, targetUserId);
        targetUserWrapper.setSql("fans_count = fans_count + 1");
        userMapper.update(null, targetUserWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unfollowUser(Long userId, Long targetUserId) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getUserId, userId);
        wrapper.eq(Follow::getFollowUserId, targetUserId);
        Follow follow = followMapper.selectOne(wrapper);
        if (follow == null) {
            throw new RuntimeException("未关注过");
        }

        followMapper.deleteById(follow.getId());

        LambdaUpdateWrapper<User> currentUserWrapper = new LambdaUpdateWrapper<>();
        currentUserWrapper.eq(User::getId, userId);
        currentUserWrapper.setSql("follow_count = follow_count - 1");
        userMapper.update(null, currentUserWrapper);

        LambdaUpdateWrapper<User> targetUserWrapper = new LambdaUpdateWrapper<>();
        targetUserWrapper.eq(User::getId, targetUserId);
        targetUserWrapper.setSql("fans_count = fans_count - 1");
        userMapper.update(null, targetUserWrapper);
    }

    @Override
    public Boolean isFollowing(Long userId, Long targetUserId) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getUserId, userId);
        wrapper.eq(Follow::getFollowUserId, targetUserId);
        return followMapper.selectCount(wrapper) > 0;
    }

    @Override
    public PageResult<UserVO> getFollowingList(Long userId, Integer page, Integer size) {
        Page<Follow> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getUserId, userId);
        wrapper.orderByDesc(Follow::getCreateTime);
        Page<Follow> followPage = followMapper.selectPage(pageParam, wrapper);

        Page<UserVO> voPage = new Page<>();
        BeanUtils.copyProperties(followPage, voPage);
        voPage.setRecords(followPage.getRecords().stream()
                .map(follow -> convertToVO(userMapper.selectById(follow.getFollowUserId())))
                .collect(java.util.stream.Collectors.toList()));

        return new PageResult<>(voPage.getTotal(), voPage.getRecords(), voPage.getCurrent(), voPage.getSize());
    }

    @Override
    public PageResult<UserVO> getFollowersList(Long userId, Integer page, Integer size) {
        Page<Follow> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getFollowUserId, userId);
        wrapper.orderByDesc(Follow::getCreateTime);
        Page<Follow> followPage = followMapper.selectPage(pageParam, wrapper);

        Page<UserVO> voPage = new Page<>();
        BeanUtils.copyProperties(followPage, voPage);
        voPage.setRecords(followPage.getRecords().stream()
                .map(follow -> convertToVO(userMapper.selectById(follow.getUserId())))
                .collect(java.util.stream.Collectors.toList()));

        return new PageResult<>(voPage.getTotal(), voPage.getRecords(), voPage.getCurrent(), voPage.getSize());
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
