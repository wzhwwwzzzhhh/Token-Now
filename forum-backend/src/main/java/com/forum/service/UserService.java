package com.forum.service;

import com.forum.dto.UserLoginDTO;
import com.forum.dto.UserRegisterDTO;
import com.forum.dto.UserUpdateDTO;
import com.forum.entity.User;
import com.forum.vo.UserVO;

public interface UserService {
    UserVO register(UserRegisterDTO dto);

    UserVO login(UserLoginDTO dto);

    UserVO getUserById(Long id);

    UserVO getCurrentUser(Long userId);

    UserVO updateUser(Long userId, UserUpdateDTO dto);
}
