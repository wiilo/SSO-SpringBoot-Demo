package com.wiilo.ssodemo.ssosupermarket.controller;

import com.alibaba.fastjson.JSON;
import com.wiilo.common.interceptor.LoginIntercept;
import com.wiilo.common.utils.ElasticsearchUtil;
import com.wiilo.common.utils.HttpResult;
import com.wiilo.ssodemo.ssosupermarket.po.ProductCommonPO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 超时相关接口
 *
 * @author Whitlock Wang
 */
@RestController
@RequestMapping("/supermarket")
public class SupermarketController {

    @Resource
    private ElasticsearchUtil elasticsearchUtil;

    @GetMapping("/goodsList")
    public List<String> queryGoodsList() {
        return Arrays.asList("手机", "水果", "玩具", "日用品", "服装", "海鲜");
    }

    @GetMapping("/goodsDetail/{goodsId}")
    @LoginIntercept
    public List<String> queryGoodsDetail(@PathVariable(value = "goodsId") Integer goodsId) {
        return Arrays.asList("手机", "水果", "玩具", "日用品", "服装", "海鲜");
    }

    @PostMapping("/addEsData")
    public HttpResult<Boolean> testInsertEsData(@RequestBody String bb) {
        ProductCommonPO productCommonPO = new ProductCommonPO()
                .setProductCommonId(1L)
                .setSpuId(10000L)
                .setCommonName("测试")
                .setProductState(0)
                .setCreateTime(new Date())
                .setUpdateTime(new Date())
                .setBrandId(1);
        Boolean testt = elasticsearchUtil.insertIndexWithJsonStr("testt", "10000", JSON.toJSONString(productCommonPO));
        return HttpResult.success(testt);
    }

    @GetMapping("/getEsData")
    public HttpResult<String> getEsData() {
        String testt = elasticsearchUtil.queryIndex("testt", "10000");
        return HttpResult.success(testt);
    }

}
