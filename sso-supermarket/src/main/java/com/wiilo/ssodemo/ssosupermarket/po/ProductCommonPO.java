package com.wiilo.ssodemo.ssosupermarket.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品spu表
 *
 * @author Whitlock Wang
 */
@Data
@Accessors(chain = true)
@TableName("product_common")
public class ProductCommonPO implements Serializable {

    private static final long serialVersionUID = 2596137960497700265L;

    /**
     * 商品主键Id
     */
    @TableId(type = IdType.AUTO)
    private Long productCommonId;
    /**
     * 商品唯一的SPUID
     */
    @TableField
    private Long spuId;
    /**
     * 商品名称
     */
    @TableField
    private String commonName;
    /**
     * 品牌id
     */
    @TableField
    private Integer brandId;
    /**
     * 店铺id
     */
    @TableField
    private Integer storeId;
    /**
     * 一级分类id
     */
    @TableField
    private Integer classIdOne;
    /**
     * 二级分类id
     */
    @TableField
    private Integer classIdTwo;
    /**
     * 三级分类id
     */
    @TableField
    private Integer classIdThree;
    /**
     * 商品内容
     */
    @TableField
    private String productBody;
    /**
     * 商品状态 0下架，1正常，10违规（禁售）
     */
    @TableField
    private Integer productState;
    /**
     * 商品主图
     */
    @TableField
    private String commonImage;
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
    /**
     * 上架时间
     */
    @TableField
    private Integer selfTime;

}
