package com.wiilo.ssodemo.ssosupermarket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wiilo.ssodemo.ssosupermarket.po.ProductCommonPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品spu表
 *
 * @author Whitlock Wang
 */
@Mapper
public interface ProductCommonMapper extends BaseMapper<ProductCommonPO> {

}
