package com.wiilo.ssodemo.ssocertification.controller;

import com.wiilo.ssodemo.ssocertification.dao.UserInfoMapper;
import com.wiilo.ssodemo.ssocertification.po.UserInfoPO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 鉴权相关接口
 *
 * @author Whitlock Wang
 * @date 2022/8/10 17:38
 */
@RefreshScope
@RestController
@RequestMapping("/cert")
public class CertificationController {

    @Value("${userInfo.name}")
    private String userName;

    @Value("${userInfo.password}")
    private String password;

    @Resource
    private UserInfoMapper userInfoMapper;

    @GetMapping("/loginCert/{id}")
    public String loginCert(@PathVariable(value = "id") Integer id) {
        UserInfoPO userInfoPO = userInfoMapper.selectById(id);
        return userInfoPO.getName() + " + " + userName + " + " + password;
    }

    @PostMapping("/addUserInfo")
    public Integer addUserInfo(@RequestBody UserInfoPO userInfoPO) {
        return userInfoMapper.insert(userInfoPO);
    }

}
