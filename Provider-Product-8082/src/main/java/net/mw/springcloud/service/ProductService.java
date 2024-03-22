package net.mw.springcloud.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.mw.springcloud.pojo.po.ProductPO;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

public interface ProductService{


    public Map getList(PageRequest page);

    public Map getByPoId(Long id);

    public Map savePo(ProductPO po);

    public Map updatePo(ProductPO po);

    public Map delById(String id);
}
