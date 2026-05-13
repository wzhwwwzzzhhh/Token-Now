package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.forum.constant.JwtConstant;
import com.forum.dto.UserLoginDTO;
import com.forum.dto.UserRegisterDTO;
import com.forum.dto.UserUpdateDTO;
import com.forum.entity.User;
import com.forum.mapper.UserMapper;
import com.forum.properties.JwtProperties;
import com.forum.service.UserService;
import com.forum.utils.JwtUtil;
import com.forum.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO register(UserRegisterDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User existUser = userMapper.selectOne(wrapper);
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setPhone(dto.getPhone());
        user.setFollowCount(0);
        user.setFansCount(0);
        user.setStatus(1);
        userMapper.insert(user);

        UserVO vo = convertToVO(user);
        vo.setToken(generateToken(user));
        return vo;
    }

    @Override
    public UserVO login(UserLoginDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, dto.getPhone());
        wrapper.eq(User::getPassword, dto.getPassword());
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new RuntimeException("手机号或密码错误");
        }
        UserVO vo = convertToVO(user);
        vo.setToken(generateToken(user));
        return vo;
    }

    @Override
    public UserVO getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    public UserVO getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO updateUser(Long userId, UserUpdateDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userId);
        if (dto.getAvatar() != null) {
            wrapper.set(User::getAvatar, dto.getAvatar());
        }
        if (dto.getBio() != null) {
            wrapper.set(User::getBio, dto.getBio());
        }
        if (dto.getEmail() != null) {
            wrapper.set(User::getEmail, dto.getEmail());
        }
        userMapper.update(null, wrapper);

        return getCurrentUser(userId);
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    private String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtConstant.UserId, user.getId());
        claims.put(JwtConstant.Phone, user.getPhone());
        return JwtUtil.generateToken(jwtProperties.getSecretKey(), jwtProperties.getTtl(), claims);
    }
}
