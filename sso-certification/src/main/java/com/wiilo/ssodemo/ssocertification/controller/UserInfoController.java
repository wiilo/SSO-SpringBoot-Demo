package com.wiilo.ssodemo.ssocertification.controller;

import com.wiilo.common.interceptor.LoginIntercept;
import com.wiilo.common.utils.JwtUtil;
import com.wiilo.ssodemo.ssocertification.po.UserInfoPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserInfoPO userInfoPO) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(userInfoPO.getUserName()) || StringUtils.isBlank(userInfoPO.getPassword())) {
            map.put("status", "请输入正确的用户名和密码");
            return map;
        }
        Map<String, String> payload = new HashMap<>();
        payload.put("userName", userInfoPO.getUserName());
        payload.put("password", userInfoPO.getPassword());
        String token = JwtUtil.generateToken(payload);
        map.put("status", "OK");
        map.put("token", token);
        return map;
    }

    @GetMapping("/getUserInfo/{userName}")
    @LoginIntercept
    public UserInfoPO getUserInfoByUserName(@PathVariable(value = "userName") String userName) {
        if (StringUtils.isBlank(userName)) {
            return null;
        }
        if ("张三".equals(userName)) {
            return new UserInfoPO().setUserName("法外狂徒").setPassword("123456");
        }
        return null;
    }

}
