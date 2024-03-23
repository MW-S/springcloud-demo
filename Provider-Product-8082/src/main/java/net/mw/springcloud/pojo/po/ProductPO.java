package net.mw.springcloud.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author W_Messi
 * @Description 车辆实体
 * @CreateTime 2022/3/27 22:23
 */
@Data
@TableName("product_index")
public class ProductPO {

    /**
     * 主键
     */
    @TableId(value="id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;
    /**
     * 创建时间
     */
    private Date CreateTime;

}
