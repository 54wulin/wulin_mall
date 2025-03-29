package com.wulin.wl_mall_tiny.common.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtTokenUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /*
     * 根据负责生成jwt的token*/
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.ES512, secret)
                .compact();
    }


    /*
     * 从token中获取jwt中的负载*/
    private Claims getClaimsFromToken(String token){
        Claims claims = null;
        try{
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch(Exception e){
            LOGGER.info("JWT格式验证失败：{}",token);
        }
        return claims;
    }



    /*
     * 生成token的过期事件*/
    private Date generateExpirationDate(){
        return new Date(System.currentTimeMillis() + expiration*1000);
    }


    /*
     * 从token中获取登陆的用户名*/
    public String getUserNameFromToken(String token){
        String username;
        try{
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        }catch(Exception e){
            username = null;
        }
        return username;
    }


    /*
     * 验证token是否还有效*/
    public boolean validateToken(String token , UserDetails userDetails){
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername())&&!isTokenExpired(token);
    }


    /*
     * 判断token是否已经失效*/
    private boolean isTokenExpired(String token){
        Date expiredDate = getExpiredDateFormToken(token);
        return expiredDate.before(new Date());
    }

    /*
     * 从token中获取过期时间*/
    private Date getExpiredDateFormToken(String token){
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }


    /*
     * 根据用户信息生成token*/
    public String generateToken(UserDetails userDetails) {
        Map<String ,Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }


    /*
     * 判断token是否可以被刷新*/
    public boolean canRefresh(String token){
        return !isTokenExpired(token);
    }


    /*
    * 刷新token*/
    public String refreshToken(String token){
        Claims claims  = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

}
