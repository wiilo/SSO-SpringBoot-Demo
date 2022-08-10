package com.wiilo.ssodemo.ssomember.controller;

import com.wiilo.common.interceptor.LoginIntercept;
import com.wiilo.ssodemo.ssomember.feign.CertificationFeignServer;
import com.wiilo.ssodemo.ssomember.po.MemberInfoPO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 会员相关接口
 *
 * @author Whitlock Wang
 * @date 2022/8/10 15:09
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private CertificationFeignServer certificationFeignServer;

    @GetMapping("/getMemberInfo/{id}")
    @LoginIntercept
    public MemberInfoPO getUserInfoByUserName(@PathVariable(value = "id") Integer id) {
        if (null == id) {
            return null;
        }
        if (1 == id) {
            return new MemberInfoPO().setMemberName("法外狂徒").setAge(12).setGender("男");
        }
        return null;
    }

    @GetMapping("/testFeign/{name}")
    public String getUserInfoByUserName(@PathVariable(value = "name") String name) {
        return certificationFeignServer.loginCert(name);
    }

}
