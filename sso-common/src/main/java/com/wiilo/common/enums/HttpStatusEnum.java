package com.wiilo.common.enums;

import com.wiilo.common.utils.ResponseIEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 接口状态枚举类
 *
 * @author Whitlock Wang
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum HttpStatusEnum implements ResponseIEnum {

    /**
     * 失败
     */
    FAILED(5000, "failed"),
    /**
     * 成功
     */
    SUCCESS(2000, "success"),
    /**
     * 资源未找到
     */
    NOT_FOUND(4040, "Not Found!"),
    /**
     * 错误的请求
     */
    BAD_REQUEST(4000, "Bad Request!"),
    /**
     * 方法不支持
     */
    METHOD_NOT_SUPPORTED(4050, "Method Not Supported!"),
    /**
     * 请求体不支持
     */
    REQUEST_BODY_NOT_READABLE(4060, "Request Body Not Readable!");

    /**
     * 状态码
     */
    private long code;
    /**
     * 提示信息
     */
    private String message;

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return message;
    }

}
