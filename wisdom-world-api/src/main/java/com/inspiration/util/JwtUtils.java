package com.inspiration.util;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SunDocker
 */
public class JwtUtils {

    private static final String JWT_SECRET = "Wisdom-World-Signature";

    public static String createToken(Long userId) {
        return Jwts.builder()
                // 签发算法，秘钥为jwtToken
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                // body数据，要唯一，自行设置
                .setSubject(String.valueOf(userId))
                // 设置签发时间
                .setIssuedAt(new Date())
                // 一天的有效时间
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .compact();
    }

    public static String checkToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
