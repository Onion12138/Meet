package com.ecnu.utils;

import com.ecnu.domain.User;
import com.ecnu.enums.ResultEnum;
import com.ecnu.exception.MyException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author onion
 * @date 2019/12/11 -9:09 上午
 */
public class JwtUtil {
    private static final String key = "meetHere";
    private static final long ttl = 60 * 60 * 24 * 1000;
    public static String createJwt(User user){
        long now = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder()
                .setId(user.getId())
                .setIssuedAt(new Date(now))
                .claim("role", user.getAdmin())
                .claim("nickname",user.getNickname())
                .setExpiration(new Date(now + ttl))
                .signWith(SignatureAlgorithm.HS256, key);
        return builder.compact();
    }
    public static Claims parseJwt(String jwtStr){
        try{
            return Jwts.parser().setSigningKey(key).parseClaimsJws(jwtStr).getBody();
        }catch (Exception e){
            throw new MyException(ResultEnum.INVALID_TOKEN);
        }
    }
}
