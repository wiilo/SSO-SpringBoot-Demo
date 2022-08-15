package com.wiilo.common.constant;

/**
 * JWT常量类
 *
 * @author Whitlock Wang
 */
public class JWTVerifyConstant {

    /**
     * 验签通过，且在JWT有效期内
     */
    public static final int JWT_SUCCESS_CODE = 0;

    /**
     * token信息正确，但验签超过JWT有效期
     */
    public static final int JWT_FAIL_EXPIRE = 1;

    /**
     * token信息不正确，验签直接失败
     */
    public static final int JWT_FAIL_ERROR = 2;

}
