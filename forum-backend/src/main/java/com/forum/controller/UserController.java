package com.forum.controller;

import com.forum.common.Result;
import com.forum.dto.UserLoginDTO;
import com.forum.dto.UserRegisterDTO;
import com.forum.dto.UserUpdateDTO;
import com.forum.service.UserService;
import com.forum.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<UserVO> register(@RequestBody UserRegisterDTO dto) {
        try {
            UserVO userVO = userService.register(dto);
            return Result.success("注册成功", userVO);
        } catch (Exception e) {
            log.error("注册失败", e);
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody UserLoginDTO dto) {
        try {
            UserVO userVO = userService.login(dto);
            return Result.success("登录成功", userVO);
        } catch (Exception e) {
            log.error("登录失败", e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        try {
            UserVO userVO = userService.getUserById(id);
            return Result.success(userVO);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/current")
    public Result<UserVO> getCurrentUser(@RequestAttribute("userId") Long userId) {
        try {
            UserVO userVO = userService.getCurrentUser(userId);
            return Result.success(userVO);
        } catch (Exception e) {
            log.error("获取当前用户信息失败", e);
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/current")
    public Result<UserVO> updateUser(@RequestAttribute("userId") Long userId, @RequestBody UserUpdateDTO dto) {
        try {
            UserVO userVO = userService.updateUser(userId, dto);
            return Result.success("更新成功", userVO);
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return Result.error(e.getMessage());
        }
    }
}
