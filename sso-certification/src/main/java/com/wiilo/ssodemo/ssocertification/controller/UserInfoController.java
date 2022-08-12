package com.wiilo.ssodemo.ssocertification.controller;

import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wiilo.common.interceptor.LoginIntercept;
import com.wiilo.common.utils.JwtUtil;
import com.wiilo.common.utils.RedisUtils;
import com.wiilo.ssodemo.ssocertification.dao.UserInfoMapper;
import com.wiilo.ssodemo.ssocertification.po.UserInfoPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户相关接口
 *
 * @author Whitlock Wang
 * @date 2022/8/10 17:38
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UserInfoMapper userInfoMapper;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserInfoPO userInfoPO) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(userInfoPO.getName()) || StringUtils.isBlank(userInfoPO.getPassword())) {
            map.put("status", "请输入正确的用户名和密码");
            return map;
        }
        UserInfoPO userInfo = userInfoMapper.selectOne(Wrappers.<UserInfoPO>lambdaQuery()
                .eq(UserInfoPO::getName, userInfoPO.getName())
                .eq(UserInfoPO::getPassword, userInfoPO.getPassword()));
        if (null == userInfo) {
            map.put("status", "用户不存在");
            return map;
        }
        String userId = String.valueOf(userInfo.getId());
        String token = redisUtils.getValue(userId);
        if (StringUtils.isNotBlank(token)) {
            map.put("status", "OK");
            map.put("token", token);
            return map;
        }
        Map<String, String> payload = new HashMap<>();
        payload.put("id", userId);
        payload.put("userName", userInfo.getName());
        token = JwtUtil.generateToken(payload);
        redisUtils.cacheValue(userId, token, 36000);
        map.put("status", "OK");
        map.put("token", token);
        return map;
    }

    @GetMapping("/getUserInfo")
    @LoginIntercept
    public UserInfoPO getUserInfoByUserName(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Map<String, Claim> payloadByToken = JwtUtil.getPayloadByToken(token);
        String id = payloadByToken.get("id").asString();
        return userInfoMapper.selectById(id);
    }

}
