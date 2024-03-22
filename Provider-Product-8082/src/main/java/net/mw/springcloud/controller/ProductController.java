package net.mw.springcloud.controller;

import net.mw.springcloud.pojo.po.ProductPO;
import net.mw.springcloud.pojo.vo.ProductVO;
import net.mw.springcloud.service.ProductService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


/**
 * @Description TaskController接口实现
 * @Author W_Messi
 * @CrateTime 2021-03-06 23:51:25
 */
@RestController
@RequestMapping("")
public class ProductController{
	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(ProductController.class);
	@Autowired
	protected ProductService service;
	@GetMapping(value = "/getList")
	public Map getList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page
			, @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
			){
		logger.trace("进入getList方法");
		PageRequest pageVo = null;
		if(ObjectUtils.allNotNull(page, size)){
			pageVo = PageRequest.of(page, size);
		}
		Map rs=service.getList(pageVo);
		logger.trace("退出getList方法");
		return rs;
	}

	@PostMapping(value = "/getListByVo")
	public Map getListByVo( @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
			, @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
			) throws InvocationTargetException, IllegalAccessException {
		logger.trace("进入 getListByVo 方法");
		PageRequest pageVo = null;
		if(ObjectUtils.allNotNull(page, size)){
			pageVo = PageRequest.of(page, size);
		}
		Map rs=service.getList(pageVo);
		logger.trace("退出 getListByVo 方法");
		return rs;
	}

	@GetMapping(value = "/getById")
	public Map getById(@RequestParam("id") String id){
		logger.trace("进入getById方法");
		Map rs=service.getByPoId(Long.valueOf(id));
		logger.trace("退出getById方法");
		return rs;
	}

	@PostMapping("/save")
	public Map save(@RequestBody ProductVO vo){
		logger.trace("进入 save 方法");
		ProductPO po = null;
		if(vo != null){
			po = new ProductPO();
			po.setName(vo.getName());
		}
		Map rs=service.savePo(po);
		logger.trace("退出 save 方法");
		return rs;
	}
	@PostMapping("/update")
	public Map update(@RequestBody ProductVO vo){
		logger.trace("进入 update 方法");
		ProductPO po = null;
		if(vo != null){
			po = new ProductPO();
			po.setId(Long.valueOf(vo.getId()));
			po.setName(vo.getName());
		}
		Map rs=service.updatePo(po);
		logger.trace("退出 update 方法");
		return rs;
	}

	@PostMapping(value = "/delById")
	public Map delByIds(@RequestBody ProductVO vo){
		logger.trace("进入 delById 方法");
		Map rs=service.delById(vo.getId());
		logger.trace("退出 delById 方法");
		return rs;
	}

}
