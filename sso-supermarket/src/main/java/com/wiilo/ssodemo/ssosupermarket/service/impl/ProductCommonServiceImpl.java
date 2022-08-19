package com.wiilo.ssodemo.ssosupermarket.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wiilo.ssodemo.ssosupermarket.dao.ProductCommonMapper;
import com.wiilo.ssodemo.ssosupermarket.po.ProductCommonPO;
import com.wiilo.ssodemo.ssosupermarket.service.ProductCommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("productCommonService")
@Slf4j
public class ProductCommonServiceImpl extends ServiceImpl<ProductCommonMapper, ProductCommonPO> implements ProductCommonService {

    @Override
    public IPage<ProductCommonPO> queryPage(Map<String, Object> params) {
        IPage<ProductCommonPO> page = new Page<>(0, 10);
        return this.page(page, Wrappers.<ProductCommonPO>lambdaQuery().eq(ProductCommonPO::getProductCommonId, params.get("id")));
    }

}