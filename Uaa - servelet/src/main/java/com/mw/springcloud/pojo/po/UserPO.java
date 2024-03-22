package com.mw.springcloud.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author W_Messi
 * @Description 车辆实体
 * @CreateTime 2022/3/27 22:23
 */
@Data
@TableName("user_index")
public class UserPO {

    /**
     * 主键
     */
    private Long id;

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
    private Date CreateTime;

}
