package com.swinginwind.xql.pay.controller.record;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.swinginwind.xql.pay.entity.BaseOrder;
import com.swinginwind.xql.pay.entity.TMembers;
import com.swinginwind.xql.pay.service.OrderService;

@Controller
@RequestMapping("/order")
public class BaseOrderController {
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> create(HttpServletRequest request, BaseOrder order) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (order.getAmount() == null || order.getAmount().doubleValue() <= 0) {
			result.put("status", "success");
			result.put("message", "金额不能为空");
			return result;
		}
		TMembers member = (TMembers) request.getSession().getAttribute("userInfo");
		if(member != null)
			order.setOrderUser(member.getUserid());
		order.setOrderTime(new Date());
		order.setStatus((byte) 0);
		orderService.insert(order);
		result.put("status", "success");
		result.put("orderId", order.getId());
		return result;
	}
	

}
