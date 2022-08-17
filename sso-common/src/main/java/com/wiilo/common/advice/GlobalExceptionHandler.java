package com.wiilo.common.advice;

import com.wiilo.common.enums.HttpStatusEnum;
import com.wiilo.common.exception.SsoCommonException;
import com.wiilo.common.utils.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常拦截
 *
 * @author Whitlock Wang
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理所有不可知的异常
     *
     * @param e 异常对象
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    HttpResult<?> handleException(Exception e) {
        log.error("处理所有不可知的异常", e);
        return HttpResult.failed(HttpStatusEnum.FAILED);
    }

    /**
     * 自定义异常处理
     *
     * @param e 自定义异常
     */
    @ExceptionHandler(SsoCommonException.class)
    @ResponseBody
    HttpResult<?> handleException(SsoCommonException e) {
        log.error("自定义异常处理", e);
        return HttpResult.failed(e.getCode(), e.getMessage());
    }

    /**
     * 404异常处理
     *
     * @param e 异常对象
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    HttpResult<?> handleException(NoHandlerFoundException e) {
        log.error("404异常处理", e);
        return HttpResult.failed(HttpStatusEnum.NOT_FOUND);
    }

    /**
     * 400参数错误
     *
     * @param e 异常对象
     */
    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    HttpResult<?> handleException(TypeMismatchException e) {
        log.error("400参数错误处理", e);
        return HttpResult.failed(HttpStatusEnum.BAD_REQUEST);
    }

    /**
     * 405方法不支持
     *
     * @param e 异常对象
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    HttpResult<?> handleException(HttpRequestMethodNotSupportedException e) {
        log.error("405方法不支持处理", e);
        return HttpResult.failed(HttpStatusEnum.METHOD_NOT_SUPPORTED);
    }

    /**
     * 406请求体不支持
     *
     * @param e 异常对象
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    HttpResult<?> handleException(HttpMessageNotReadableException e) {
        log.error("406请求体不支持处理", e);
        return HttpResult.failed(HttpStatusEnum.REQUEST_BODY_NOT_READABLE);
    }

}