package com.swinginwind.xql.pay.controller.record;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.swinginwind.xql.pay.entity.PayRecord;
import com.swinginwind.xql.pay.entity.TMembers;
import com.swinginwind.xql.pay.service.UserService;

@Controller
@RequestMapping("/userPage")
public class UserPageController {
	
	@Autowired
	private UserService userService;

	//获取登录用戶信息
    @RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
    @ResponseBody
    public Object getCurrentUser(HttpServletRequest request){
    	TMembers member = (TMembers) request.getSession().getAttribute("userInfo");
        if(member != null) {
        	member = userService.selectByUserId(member.getUserid());
        }
    	return member;
    }
    
    
    @RequestMapping(value = "/saveCurrentUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>  saveCurrentUser(TMembers member, HttpServletRequest request) {
    	Map<String, Object> result = new HashMap<String, Object>();
    	TMembers memberT = (TMembers) request.getSession().getAttribute("userInfo");
        if(memberT == null || !memberT.getUserid().equals(member.getUserid())) {
        	result.put("status", "error");
        }
        else {
        	member.setPassword(null);
    		userService.saveUserInfo(member);
    		result.put("status", "success");
        }
		return result;
	}
    
    
    @RequestMapping(value = "/setUser")
    @ResponseBody
    public Map<String, Object>  setUser(int userId, HttpServletRequest request) {
    	Map<String, Object> result = new HashMap<String, Object>();
    	TMembers memberT = (TMembers) request.getSession().getAttribute("userInfo");
        if(memberT == null || !memberT.getUserid().equals(userId)) {
        	memberT = userService.selectByUserId(userId);
        	request.getSession().setAttribute("userInfo", memberT);
        }
        result.put("status", "success");
		return result;
	}
	
}
