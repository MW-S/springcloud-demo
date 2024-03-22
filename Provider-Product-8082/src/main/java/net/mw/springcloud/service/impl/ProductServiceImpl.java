package net.mw.springcloud.service.impl;


import com.github.pagehelper.PageHelper;
import net.mw.springcloud.dao.ProductDao;
import net.mw.springcloud.pojo.po.ProductPO;
import net.mw.springcloud.pojo.vo.ProductVO;
import net.mw.springcloud.service.ProductService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description TaskServiceImpl接口实现
 * @Author W_Messi
 * @CrateTime 2021-03-06 21:30:03
 */
@Service
public class ProductServiceImpl implements ProductService {
    /**
     * log4j实例对象.
     */
    private static Logger logger = LogManager.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductDao dao;


    @Override
    public Map getList(PageRequest page) {
        logger.trace("进入getList方法");
        Map rs = new HashMap();
        try {

            Map<String,Object> data = new HashMap<String,Object>();
            if(ObjectUtils.allNotNull(page)){
                PageHelper.startPage(page.getPageNumber(), page.getPageSize());
            }
            List<ProductPO> pos = dao.getList();
            List<ProductVO> vos = new ArrayList<>();
            pos.forEach((item)->{
                ProductVO vo = new ProductVO();
                vo.setId(item.getId().toString());
                vo.setName(item.getName());
                vo.setCreateTime(item.getCreateTime().toString());
                vos.add(vo);
            });
            data.put("total", dao.count());
            data.put("size", vos.size());
            data.put("list", vos);
            rs.put("data", data);
            rs.put("code", 1L);
            rs.put("msg","获取成功!");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            rs.put("msg", "参数不正确");
            rs.put("code", 2L);
        } catch (Exception e) {
            e.printStackTrace();
            rs.put("msg", "获取失败");
            rs.put("code",0L);
        }
        logger.trace("退出getList方法");
        return rs;
    }

    @Override
    public Map getByPoId(Long id) {
        logger.trace("进入 getById 方法");
        Map rs = new HashMap();
        try {
            if(!ObjectUtils.allNotNull(id)){
                logger.debug("Id must not null!");
                throw new IllegalArgumentException("Id must not null!");
            }
            Map<String,Object> data = new HashMap<String,Object>();
            ProductPO resPo =  dao.getById(id);
            ProductVO vo = new ProductVO();
            if(ObjectUtils.allNotNull(resPo)){
                vo.setId(resPo.getId().toString());
                vo.setName(resPo.getName());
                vo.setCreateTime(resPo.getCreateTime().toString());
            }
            data.put("data",vo);
            rs.put("data", data);
            rs.put("code", 1L);
            rs.put("msg","获取成功!");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            rs.put("msg", "参数不正确");
            rs.put("code", 2L);
        } catch (Exception e) {
            e.printStackTrace();
            rs.put("msg", "获取失败");
            rs.put("code",0L);
        }
        logger.trace("退出 getById 方法");
        return rs;
    }

    @Override
    public Map savePo(ProductPO po) {
        logger.trace("进入add方法");
        Map rs = new HashMap();
        try {
            if(!ObjectUtils.allNotNull(po)){
                logger.debug("canteen must not null!");
                throw new IllegalArgumentException("canteen must not null!");
            }
            if(dao.insert(po) > 0) {
                rs.put("code", 1L);
                rs.put("msg", "操作成功!");
            }else {
                rs.put("code", 1L);
                rs.put("msg", "添加失败!");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            rs.put("msg", "参数不正确");
            rs.put("code", 2L);
        } catch (Exception e) {
            e.printStackTrace();
            rs.put("msg", "添加失败");
            rs.put("code",0L);
        }
        logger.trace("退出add方法");
        return rs;
    }

    @Override
    public Map updatePo(ProductPO po) {
        logger.trace("进入update方法");
        Map rs = new HashMap();
        try {
            if(!ObjectUtils.allNotNull(po)){
                logger.debug("canteen must not null!");
                throw new IllegalArgumentException("canteen must not null!");
            }
            if(dao.updateById(po) > 0) {
                rs.put("code", 1L);
                rs.put("msg", "修改成功!");
            }else {
                rs.put("code", 1L);
                rs.put("msg", "修改失败!");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            rs.put("msg", "参数不正确");
            rs.put("code", 2L);
        } catch (Exception e) {
            e.printStackTrace();
            rs.put("msg", "修改失败");
            rs.put("code",0L);
        }
        logger.trace("退出update方法");
        return rs;
    }

    @Override
    public Map delById(String id) {
        logger.trace("进入 delByIds 方法");
        Map rs = new HashMap();
        try {
            if(!ObjectUtils.allNotNull(id)){
                logger.debug("ids must not null!");
                throw new IllegalArgumentException("ids must not null!");
            }
            rs.put("msg", 1L);
            if(dao.deleteById(id) > 0) {
                rs.put("msg", "删除成功!");
            }else {
                rs.put("msg", "删除失败!");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            rs.put("msg", "参数不正确");
            rs.put("code", 2L);
        } catch (Exception e) {
            e.printStackTrace();
            rs.put("msg", "删除失败");
            rs.put("code",0L);
        }
        logger.trace("退出 delByIds 方法");
        return rs;
    }
}
