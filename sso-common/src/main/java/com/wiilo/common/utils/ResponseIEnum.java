package com.wiilo.common.utils;

/**
 * http请求枚举统一接口
 *
 * @author Whitlock Wang
 */
public interface ResponseIEnum {

    /**
     * 状态码
     */
    long getCode();

    /**
     * 提示信息
     */
    String getMsg();

}
