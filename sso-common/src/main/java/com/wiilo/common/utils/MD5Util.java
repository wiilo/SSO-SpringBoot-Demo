package com.wiilo.common.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * MD5加密工具类
 *
 * @author Whitlock Wang
 */
public class MD5Util {

    //盐，用于混交md5
    private static final String salt = "sso_salt";

    /**
     * java原生用法
     *
     * @param str    要加密的字符串
     * @param isSalt 是否加盐
     * @return java.lang.String
     * @author Whitlock Wang
     */
    public static String encrypt(String str, boolean isSalt) {
        try {
            if (isSalt) {
                str = str + salt;
            }
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] s = m.digest();
            StringBuilder result = new StringBuilder();
            for (byte b : s) {
                result.append(Integer.toHexString((0x000000FF & b) | 0xFFFFFF00).substring(6));
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * spring提供的工具类用法生成md5
     *
     * @param str    要加密的字符串
     * @param isSalt 是否加盐
     * @return java.lang.String
     * @author Whitlock Wang
     */
    public static String getMD5(String str, boolean isSalt) {
        if (isSalt) {
            str = str + salt;
        }
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }
}