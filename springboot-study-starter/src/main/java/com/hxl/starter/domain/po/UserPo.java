

package com.hxl.starter.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;


/**
 * User
 *
 * @author hxl
 * @description TODO
 * @date 2023/11/28 18:05:50
 */
@Getter
@Setter
@ToString(callSuper = true)
@TableName("user")
public class UserPo {

    /**
     * id
     */
    @TableId("id")
    private Integer id;


    /**
     * 账号
     */
    @TableField("user_name")
    private String userName;

    /**
     * 密码
     */
    @TableField("pass_word")
    private String passWord;

}