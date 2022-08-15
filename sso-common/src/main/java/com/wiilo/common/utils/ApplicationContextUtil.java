package com.wiilo.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wiilo.common.context.ServletInfo;
import com.wiilo.common.context.ServletInfoContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 应用上下文工具类
 *
 * @author Whitlock Wang
 */
@Component
public class ApplicationContextUtil {

    private static RedisUtil redisUtil;

    @Resource(name = "redisUtil")
    public void setRedisUtils(RedisUtil redisUtil) {
        ApplicationContextUtil.redisUtil = redisUtil;
    }

    /**
     * 获取token
     *
     * @return java.lang.String
     * @author Whitlock Wang
     */
    public static String getToken() {
        ServletInfo servletInfo = ServletInfoContext.get();
        if (null == servletInfo) {
            return null;
        }
        return servletInfo.getToken();
    }

    /**
     * 获取token
     *
     * @param request 请求对象
     * @return java.lang.String
     * @author Whitlock Wang
     */
    public static String getToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    /**
     * 获取用户Id
     *
     * @return java.lang.String
     * @author Whitlock Wang
     */
    public static String getUserId() {
        ServletInfo servletInfo = ServletInfoContext.get();
        if (null == servletInfo) {
            return null;
        }
        String userInfo = redisUtil.getValue(servletInfo.getToken());
        if (StringUtils.isBlank(userInfo)) {
            return null;
        }
        JSONObject userJson = JSON.parseObject(userInfo);
        return userJson.getString("id");
    }

    /**
     * 获取用户Id
     *
     * @param request 请求对象
     * @return java.lang.String
     * @author Whitlock Wang
     */
    public static String getUserId(HttpServletRequest request) {
        String sessionId = getToken(request);
        if (StringUtils.isBlank(sessionId)) {
            return null;
        }
        String userInfo = redisUtil.getValue(sessionId);
        if (StringUtils.isBlank(userInfo)) {
            return null;
        }
        JSONObject userJson = JSON.parseObject(userInfo);
        return userJson.getString("id");
    }

}
