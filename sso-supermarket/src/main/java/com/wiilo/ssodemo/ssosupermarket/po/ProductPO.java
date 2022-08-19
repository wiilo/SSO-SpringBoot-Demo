package com.wiilo.ssodemo.ssosupermarket.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品sku表
 *
 * @author Whitlock Wang
 */
@Data
@Accessors(chain = true)
@TableName("product")
public class ProductPO implements Serializable {

    private static final long serialVersionUID = -1590450552785456581L;

    /**
     * sku表主键id
     */
    @TableId(type = IdType.AUTO)
    private Long productId;
    /**
     * 商品唯一skuId
     */
    @TableField
    private Long skuId;
    /**
     * spu表主键
     */
    @TableField
    private Long productCommonId;
    /**
     * 商品名称
     */
    @TableField
    private String productName;
    /**
     * 商品售价
     */
    @TableField
    private BigDecimal productPrice;
    /**
     * 商品原价
     */
    @TableField
    private BigDecimal productOriginalPrice;
    /**
     * 商品属性
     */
    @TableField
    private String productAttribute;
    /**
     * 商品库存
     */
    @TableField
    private Integer productStorage;
    /**
     * sku图片
     */
    @TableField
    private String productImage;
    /**
     * 商品状态 0下架，1正常，10违规（禁售）
     */
    @TableField
    private Integer productState;
    /**
     * 创建时间
     */
    @TableField
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField
    private Date updateTime;

}
