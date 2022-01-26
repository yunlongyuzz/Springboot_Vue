package com.yyl.myblog.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

/**
 * jwt工具类
 */
@Slf4j
@Data
@Component
public class JwtUtils {

    //密钥
    private static final String secret = "0s9l6xCowiuzRwXClFfyXQ00Trn0y88sk8sP0aYp";
    //到期时间
    private long expire = 3600 * 24 * 7 * 1000;

    //签名使用的签名算法和签名使用的秘钥
    public static Key genKey() {
        Key sKey = Keys.hmacShaKeyFor(Base64.encode(secret).getBytes());
        return sKey;
    }

    /**
     * 生成jwt token
     */
    public String generateToken(long userId) {

        JwtBuilder builder = Jwts.builder();

        //当前时间
        long time = System.currentTimeMillis();

        //jwt的签发时间,Date类型
        Date now = new Date(time);
        builder.setIssuedAt(now);

        //设置过期时间
        if (expire >= 0){
            //当前时间加上过期时间
            long expMillis = time + expire;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //设置JWT ID,是JWT的唯一标识
        String id = IdUtil.objectId();
        //jwt签发人
        String issuer = "YunLong";

        //设置JWT ID,是JWT的唯一标识
        builder.setId(id).
                //jwt签发人
                setIssuer(issuer).
                //设置token的类型：JWT
                setHeaderParam("typ", "JWT").
                //设置jwt主体
                setSubject(userId + "").
                // 设置签名使用的签名算法和签名使用的秘钥
                signWith(genKey());

        //返回jwtBuilder转化为一个字符串
        return builder.compact();
    }

    /**
     * 解密token
     * @param token
     * @return
     */
    public Claims getClaimByToken(String token) {

            Claims claims = Jwts.parserBuilder().setSigningKey(genKey()).build().
                parseClaimsJws(token).getBody();

            return claims;


    }

    /**
     * token是否过期
     * @return  true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}

