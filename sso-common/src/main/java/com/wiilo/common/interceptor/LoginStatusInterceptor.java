package com.wiilo.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wiilo.common.constant.JWTVerifyConstant;
import com.wiilo.common.constant.SystemConstant;
import com.wiilo.common.context.ServletInfo;
import com.wiilo.common.context.ServletInfoContext;
import com.wiilo.common.enums.SystemErrorEnum;
import com.wiilo.common.exception.SsoCommonException;
import com.wiilo.common.utils.JwtUtil;
import com.wiilo.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 */
@Component
@Slf4j
public class LoginStatusInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
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
            // 存在token直接设置一次
            if (null != token) {
                response.addHeader("Authorization", token);
            }
            return true;
        }
        if (!redisUtil.containsValueKey(token)) {
            throw new SsoCommonException(SystemErrorEnum.NEED_LOGIN);
        }
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(token)) {
            Integer verifyCode = JwtUtil.exceptionVerify(token);
            switch (verifyCode) {
                case JWTVerifyConstant.JWT_SUCCESS_CODE:
                    response.setHeader("Authorization", token);
                    ServletInfoContext.save(new ServletInfo(token));
                    log.info("登录状态验证成功");
                    return true;
                case JWTVerifyConstant.JWT_FAIL_EXPIRE:
                    String newToken = JwtUtil.refreshToken(token);
                    response.setHeader("Authorization", newToken);
                    ServletInfoContext.save(new ServletInfo(newToken));
                    //刷新redis用户信息的缓存
                    String userInfo = redisUtil.getValue(token);
                    JSONObject userJson = JSON.parseObject(userInfo);
                    String id = userJson.getString("id");
                    redisUtil.cacheValue(id, newToken, SystemConstant.LOGIN_TIME_OUT_DAY);
                    redisUtil.cacheValue(newToken, userInfo, SystemConstant.LOGIN_TIME_OUT_DAY);
                    redisUtil.removeValue(token);
                    log.info("登录状态超过jwt有效时间，但是信息正确，验证通过并刷新过期时间");
                    return true;
                case JWTVerifyConstant.JWT_FAIL_ERROR:
                    log.info("登录信息不正确，需要重新登录");
                    map.put("msg", "token无效");
                default:
                    log.info("登录信息不正确，需要重新登录");
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
