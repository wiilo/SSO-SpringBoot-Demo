package com.wiilo.ssodemo.ssomember.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 会员信息对象
 *
 * @author Whitlock Wang
 * @date 2022/8/10 15:11
 */
@Data
@Accessors(chain = true)
public class MemberInfoPO implements Serializable {

    private Integer id;
    private String memberName;
    private Integer age;
    private String gender;
}
