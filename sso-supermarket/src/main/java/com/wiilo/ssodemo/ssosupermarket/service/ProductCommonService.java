package com.wiilo.ssodemo.ssosupermarket.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wiilo.ssodemo.ssosupermarket.po.ProductCommonPO;

import java.util.Map;

/**
 * 商品spu表
 *
 * @author Whitlock Wang
 */
public interface ProductCommonService extends IService<ProductCommonPO> {

    IPage<ProductCommonPO> queryPage(Map<String, Object> params);

}

