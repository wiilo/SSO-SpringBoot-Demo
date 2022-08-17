package com.wiilo.common.utils;

import com.wiilo.common.enums.HttpStatusEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口统一返回实体对象
 *
 * @author Whitlock Wang
 */
@Data
public class HttpResult<T> implements Serializable {

    private static final long serialVersionUID = 2650766717015821499L;

    /**
     * 状态code
     */
    private long code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 数据
     */
    private T data;

    private HttpResult() {
    }

    private HttpResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 请求返回模板方法
     *
     * @param code    状态码
     * @param message 提示信息
     * @param data    数据
     * @return com.wiilo.common.utils.Result<T>
     * @author Whitlock Wang
     */
    public static <T> HttpResult<T> restResult(long code, String message, T data) {
        return new HttpResult<>(code, message, data);
    }

    /**
     * 请求返回模板方法
     *
     * @param responseIEnum http请求枚举统一接口
     * @param data          数据
     * @return com.wiilo.common.utils.Result<T>
     * @author Whitlock Wang
     */
    public static <T> HttpResult<T> restResult(ResponseIEnum responseIEnum, T data) {
        return restResult(responseIEnum.getCode(), responseIEnum.getMsg(), data);
    }

    /**
     * 请求成功返回方法
     *
     * @param data 数据
     * @return com.wiilo.common.utils.Result<T>
     * @author Whitlock Wang
     */
    public static <T> HttpResult<T> success(T data) {
        HttpStatusEnum statusEnum = HttpStatusEnum.SUCCESS;
        if (data instanceof Boolean && Boolean.FALSE.equals(data)) {
            statusEnum = HttpStatusEnum.FAILED;
        }
        return restResult(statusEnum, data);
    }

    /**
     * 请求失败返回方法
     *
     * @param responseIEnum http请求枚举统一接口
     * @return com.wiilo.common.utils.Result<T>
     * @author Whitlock Wang
     */
    public static <T> HttpResult<T> failed(ResponseIEnum responseIEnum) {
        return restResult(responseIEnum, null);
    }

    /**
     * 请求失败返回方法
     *
     * @param responseIEnum http请求枚举统一接口
     * @param data          数据
     * @return com.wiilo.common.utils.Result<T>
     * @author Whitlock Wang
     */
    public static <T> HttpResult<T> failed(ResponseIEnum responseIEnum, T data) {
        return restResult(responseIEnum, data);
    }

    /**
     * 请求失败返回方法
     *
     * @param code    状态码
     * @param message 提示信息
     * @return com.wiilo.common.utils.Result<T>
     * @author Whitlock Wang
     */
    public static <T> HttpResult<T> failed(long code, String message) {
        return restResult(code, message, null);
    }

}
