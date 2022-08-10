package com.wiilo.ssodemo.ssomember.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用鉴权服务
 *
 * @author Whitlock Wang
 * @date 2022/8/10 17:38
 */
@FeignClient(name = "sso-certification", url = "192.168.8.78:1010")
public interface CertificationFeignServer {

    @PostMapping("cert/loginCert")
    String loginCert(@RequestBody String name);

}
