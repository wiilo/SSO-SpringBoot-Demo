package com.wiilo.ssodemo.ssosupermarket.controller;

import com.wiilo.common.interceptor.LoginIntercept;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 超时相关接口
 *
 * @author Whitlock Wang
 * @date 2022/8/10 15:15
 */
@RestController
@RequestMapping("/supermarket")
public class SupermarketController {

    @GetMapping("/goodsList")
    public List<String> queryGoodsList() {
        return Arrays.asList("手机", "水果", "玩具", "日用品", "服装", "海鲜");
    }

    @GetMapping("/goodsDetail/{goodsId}")
    @LoginIntercept
    public List<String> queryGoodsDetail(@PathVariable(value = "goodsId") Integer goodsId) {
        return Arrays.asList("手机", "水果", "玩具", "日用品", "服装", "海鲜");
    }

}
