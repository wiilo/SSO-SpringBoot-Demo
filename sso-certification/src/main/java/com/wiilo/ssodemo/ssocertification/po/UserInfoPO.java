package com.wiilo.ssodemo.ssocertification.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户信息对象
 *
 * @author Whitlock Wang
 * @date 2022/8/9 16:34
 */
@Data
@Accessors(chain = true)
@TableName("user_info")
public class UserInfoPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;
    @TableField
    private String name;
    @TableField
    private String password;

}
