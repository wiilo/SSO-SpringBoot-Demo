package com.wiilo.ssodemo.ssocertification.po;

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
public class UserInfoPO implements Serializable {

    private String userName;
    private String password;

}
