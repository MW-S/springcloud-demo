package com.mw.springcloud.pojo.vo;

import lombok.Data;

/**
 * @Description 饭堂实体
 * @author W_Messi
 * @CreateTime 2022/3/27 22:23
 *
 */
@Data
public class UserVO {

    /**
     * 主键
     */
    private String id;
    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 创建时间
     */
   private String CreateTime;
}
