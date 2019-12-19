package com.swinginwind.xql.pay.controller.record;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.swinginwind.xql.pay.entity.BaseProduct;
import com.swinginwind.xql.pay.mapper.BaseProductMapper;

@Controller
@RequestMapping("/baseProduct")
public class BaseProductController {
	
	@Autowired
	private BaseProductMapper baseProductMapper;
	
	@RequestMapping("/query")
	@ResponseBody
	public List<BaseProduct>  query() {
		return baseProductMapper.getAll();
	}
	

}
