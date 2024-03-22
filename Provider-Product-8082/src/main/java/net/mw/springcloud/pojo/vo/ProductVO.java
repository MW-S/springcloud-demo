package net.mw.springcloud.pojo.vo;

import lombok.Data;

/**
 * @Description 饭堂实体
 * @author W_Messi
 * @CreateTime 2022/3/27 22:23
 *
 */
@Data
public class ProductVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;
    /**
     * 创建时间
     */
   private String CreateTime;
}
