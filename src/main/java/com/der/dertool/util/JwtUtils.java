package com.der.dertool.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-22 13:44
 */
public class JwtUtils {

    //设置token过期时间-15分钟
    public static final Long EXPIRE_TIME = 15 * 60 * 1000L;

    //设置token私钥
    private static final String SECRET_KEY = "long-derdrache666666";

    /*
     * 生成签名
     *
     */
    public static String sign(String username, String password) {
        try {
            // 设置过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");
            // 返回token字符串
            return JWT.create()
                    .withHeader(header)
                    .withClaim("username", username)
                    .withClaim("pwd", password)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检验token是否正确
     * @param **token**
     * @return
     */
    public static boolean verify(String token, String username, String password){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .withClaim("pwd", password)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * 从token中获取username信息,无需解密
     * @param **token**
     * @return
     */
    public static String getUserName(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            if(System.currentTimeMillis()-jwt.getExpiresAt().getTime()>0){
                return null;
            }
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
//        String token = sign("K0790016", "Admin@123");
//        System.out.println("token: " + token);
//        String userName = getUserName(token);
//        System.out.println("username: " + userName);
        boolean verify = verify("eyJhbGciOiJIUzI1NiIsIlR5cGUiOiJKd3QiLCJ0eXAiOiJKV1QifQ.eyJwd2QiOiJBZG1pbkAxMjMiLCJleHAiOjE1OTUzOTgzMTcsInVzZXJuYW1lIjoiSzA3OTAwMTYifQ.RFGg5cXxHH4NxWHtMeJIcOZIPKD1ppAVnHl0MkkY48g", "K0790016", "Admin@123");
        System.out.println("验证结果: " + verify);
    }

}
