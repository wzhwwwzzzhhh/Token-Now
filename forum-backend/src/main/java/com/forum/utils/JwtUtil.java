package com.forum.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    /**
     * 生成token
     */
    public static String generateToken(String secretKey, Long ttlmillis, Map<String,Object> claims) {
        //指定算法签名
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成jwt得时间
        long nowMillis = System.currentTimeMillis()+ttlmillis;
        //过期时间
        Date expDate = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expDate)
                .signWith(signatureAlgorithm,secretKey.getBytes(StandardCharsets.UTF_8));
        return builder.compact();
    }

    /**
     * 解析token
     */
    public static Claims parseToken(String token, String secretKey) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
}
