package com.forum.Inteceptor;

import com.forum.constant.JwtConstant;
import com.forum.context.BaseContext;
import com.forum.properties.JwtProperties;
import com.forum.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader(jwtProperties.getTokenName());
        if (token == null || token.isEmpty()) {
            // 没有token，匿名访问，设置 userId 为 null 即可放行
            BaseContext.setCurrentId(null);
            request.setAttribute("userId", null);
            return true;
        }

        try {
            log.info("jwt校验: {}", token);
            Claims claims = JwtUtil.parseToken(token, jwtProperties.getSecretKey());
            Long userId = Long.valueOf(claims.get(JwtConstant.UserId).toString());
            log.info("jwt校验成功，用户id: {}", userId);
            BaseContext.setCurrentId(userId);
            request.setAttribute("userId", userId);
            return true;
        } catch (Exception e) {
            log.error("jwt校验失败: {}", e.getMessage());
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"token无效或已过期\"}");
            return false;
        }
    }
}
