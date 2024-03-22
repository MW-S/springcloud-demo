package net.mw.springcloud.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.mw.springcloud.pojo.po.ProductPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProductDao extends BaseMapper<ProductPO> {

    @Select("select * from product_index ")
    public List<ProductPO>  getList();
    @Select("select count(*) from product_index ")
    public Integer count();
    @Select("select * from product_index where id = #{id}")
    public ProductPO getById(Long id);
}
