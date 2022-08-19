package com.wiilo.ssodemo.ssosupermarket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wiilo.ssodemo.ssosupermarket.po.ProductPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品sku表
 *
 * @author Whitlock Wang
 */
@Mapper
public interface ProductMapper extends BaseMapper<ProductPO> {

}
