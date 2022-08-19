package com.wiilo.ssodemo.ssosupermarket.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wiilo.ssodemo.ssosupermarket.dao.ProductMapper;
import com.wiilo.ssodemo.ssosupermarket.po.ProductPO;
import com.wiilo.ssodemo.ssosupermarket.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("productService")
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductPO> implements ProductService {

    @Override
    public IPage<ProductPO> queryPage(Map<String, Object> params) {
        IPage<ProductPO> page = new Page<>(0, 10);
        return this.page(page, Wrappers.<ProductPO>lambdaQuery().eq(ProductPO::getProductId, params.get("id")));
    }

}