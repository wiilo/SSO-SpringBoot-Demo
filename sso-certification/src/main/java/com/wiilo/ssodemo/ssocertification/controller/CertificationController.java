package com.wiilo.ssodemo.ssocertification.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 鉴权相关接口
 *
 * @author Whitlock Wang
 * @date 2022/8/10 17:38
 */
@RestController
@RequestMapping("/cert")
public class CertificationController {

    @PostMapping("/loginCert")
    public String loginCert(@RequestBody String params) {
        return params;
    }

}
