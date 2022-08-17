package com.wiilo.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wiilo.common.constant.JWTVerifyConstant;
import com.wiilo.common.constant.SystemConstant;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Jwt工具类
 *
 * @author Whitlock Wang
 */
public class JwtUtil {

    private static final String SECRET = "CCMetric";

    /**
     * 生成token
     *
     * @param payload 载荷,需要在token中存放的数据
     * @return java.lang.String
     * @author Whitlock Wang
     */
    public static String generateToken(Map<String, String> payload) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, SystemConstant.ONE_INT);
        JWTCreator.Builder builder = JWT.create();
        //载荷,生成token中保存的信息
        payload.forEach(builder::withClaim);
        return builder.withAudience("admin") //签发对象
                .withIssuedAt(new Date()) //发行时间
                .withExpiresAt(instance.getTime()) //过期时间
                .sign(Algorithm.HMAC256(SECRET)); //加密算法+盐
    }

    /**
     * 刷新token
     *
     * @param token 用户令牌
     * @return java.lang.String
     * @author Whitlock Wang
     */
    public static String refreshToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        // 获取用户id
        Claim userId = claims.get("id");
        // 获取用户名
        Claim name = claims.get("userName");
        // 获取发行时间
        Claim issueAt = claims.get("iat");
        // 生成过期时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, SystemConstant.ONE_INT);
        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        return JWT.create()
                .withHeader(map)
                .withAudience("admin") //签发对象
                .withClaim("userId", userId.asString())
                .withClaim("name", name.asString())
                .withIssuedAt(issueAt.asDate())
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 校验token,有异常,即为校验失败
     *
     * @param token 用户令牌
     * @return com.auth0.jwt.interfaces.DecodedJWT
     * @author Whitlock Wang
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    /**
     * 返回异常code的验签
     *
     * @param token 用户令牌
     * @return java.lang.Integer
     * @author Whitlock Wang
     */
    public static Integer exceptionVerify(String token) {
        try {
            verify(token);
            return JWTVerifyConstant.JWT_SUCCESS_CODE;
        } catch (TokenExpiredException e) {
            return JWTVerifyConstant.JWT_FAIL_EXPIRE;
        } catch (Exception e) {
            return JWTVerifyConstant.JWT_FAIL_ERROR;
        }
    }

    /**
     * 根据token获取载荷信息
     *
     * @param token 用户令牌
     * @return java.util.Map<java.lang.String, com.auth0.jwt.interfaces.Claim>
     * @author Whitlock Wang
     */
    public static Map<String, Claim> getPayloadByToken(String token) {
        return verify(token).getClaims();
    }

}
