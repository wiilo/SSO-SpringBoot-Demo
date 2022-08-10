package com.wiilo.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.wiilo.common.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录拦截器
 *
 * @author Whitlock Wang
 * @date 2022/8/9 16:58
 */
@Component
public class LoginStatusInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Class<?> clazz = handlerMethod.getBeanType();
        Method method = handlerMethod.getMethod();
        LoginIntercept loginIntercept = null;
        boolean isClzAnnotation = clazz.isAnnotationPresent(LoginIntercept.class);
        boolean isMethondAnnotation = method.isAnnotationPresent(LoginIntercept.class);
        //如果方法和类声明中同时存在这个注解，那么方法中的会覆盖类中的设定。
        if (isMethondAnnotation) {
            loginIntercept = method.getAnnotation(LoginIntercept.class);
        } else if (isClzAnnotation) {
            loginIntercept = clazz.getAnnotation(LoginIntercept.class);
        }
        String token = request.getHeader("Authorization");
        if (loginIntercept == null) {
            // 如果注解为null, 说明不需要拦截, 直接放过
            // 存在的话进行session操作
            if (null != token) {
                response.addHeader("Auth-Token", token);
            }
            return true;
        }
        Map<String, Object> map = new HashMap<>();
        if (token != null) {
            try {
                JwtUtil.verify(token);
                return true;
            } catch (Exception e) {
                map.put("msg", "token无效");
            }
        } else {
            map.put("msg", "token为空");
        }
        map.put("status", false);
        //错误信息响应到前台
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(JSON.toJSONString(map));
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return false;
    }
}
