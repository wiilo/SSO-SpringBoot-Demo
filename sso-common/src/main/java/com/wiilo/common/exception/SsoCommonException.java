package com.wiilo.common.exception;

import com.wiilo.common.utils.ResponseIEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 自定义最高层异常类
 *
 * @author Whitlock Wang
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SsoCommonException extends RuntimeException {

    private static final long serialVersionUID = -8742678469409793800L;

    private long code;

    private String message;

    public SsoCommonException() {
        super();
    }

    public SsoCommonException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.message = msg;
    }

    public SsoCommonException(ResponseIEnum responseIEnum) {
        super(responseIEnum.getMsg());
        this.code = responseIEnum.getCode();
        this.message = responseIEnum.getMsg();
    }

    public SsoCommonException(Integer code, String msg, Throwable t) {
        super(msg, t);
        this.code = code;
        this.message = msg;
    }

    public SsoCommonException(ResponseIEnum responseIEnum, Throwable t) {
        super(responseIEnum.getMsg(), t);
        this.code = responseIEnum.getCode();
        this.message = responseIEnum.getMsg();
    }

    public SsoCommonException(ResponseIEnum responseIEnum, String msg) {
        super(responseIEnum.getMsg());
        this.code = responseIEnum.getCode();
        this.message = String.format(responseIEnum.getMsg(), msg);
    }

}
