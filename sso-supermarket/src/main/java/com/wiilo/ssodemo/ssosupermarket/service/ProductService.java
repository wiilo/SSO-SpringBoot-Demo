package com.wiilo.ssodemo.ssosupermarket.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wiilo.ssodemo.ssosupermarket.po.ProductPO;

import java.util.Map;

/**
 * 商品sku表
 *
 * @author Whitlock Wang
 */
public interface ProductService extends IService<ProductPO> {

    IPage<ProductPO> queryPage(Map<String, Object> params);
}

