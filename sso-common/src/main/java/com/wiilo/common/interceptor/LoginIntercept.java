package com.wiilo.common.interceptor;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 登录拦截注解
 *
 * @author Whitlock Wang
 * @date 2022/8/9 16:56
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface LoginIntercept {

    /**
     * 默认为空 需要验证登录状态，
     *
     * @return
     */
    String value() default "";

}
