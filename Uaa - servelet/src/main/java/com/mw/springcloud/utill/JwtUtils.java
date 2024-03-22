package com.mw.springcloud.utill;

import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtils {


    /**
     * 生成token字符串  xxx.yyy.zzzz
     * @param username
     * @return
     */
    public static  String getToken(String username){

        return Jwts.builder().setHeaderParam("typ", "JWT")
                //过期时间  一个小时后此token就失效了
                .setExpiration(new Date(System.currentTimeMillis()+3600000))
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, "jf3q-jwt").compact();
    }

    //校验token

    public static Boolean validate(String token ){
        try {
            Jwts.parser().setSigningKey("jf3q-jwt").parseClaimsJws(token).getBody();//拿到jwt令牌里的载荷部分
            return true;
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("token已经过期");
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("不支持的token");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("token令牌格式不对");
        } catch (SignatureException e) {
            throw new RuntimeException("token签名问题");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("参数不合法-密钥不对");
        }

    }

    public static String getUserName(String token){

        String username = null;
        try {
            username = Jwts.parser().setSigningKey("jf3q-jwt").parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("token已经过期");
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("不支持的token");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("token令牌格式不对");
        } catch (SignatureException e) {
            throw new RuntimeException("token签名问题");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("参数不合法-密钥不对");
        }
        return username;

    }
}