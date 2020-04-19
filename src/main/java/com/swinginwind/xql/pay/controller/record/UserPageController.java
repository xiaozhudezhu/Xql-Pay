package com.swinginwind.xql.pay.controller.record;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.swinginwind.xql.pay.config.AppConfig;
import com.swinginwind.xql.pay.entity.PayRecord;
import com.swinginwind.xql.pay.entity.TMembers;
import com.swinginwind.xql.pay.service.UserService;

@Controller
@RequestMapping("/userPage")
public class UserPageController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AppConfig appConfig;

	//获取登录用戶信息
    @RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
    @ResponseBody
    public Object getCurrentUser(HttpServletRequest request){
    	TMembers member = (TMembers) request.getSession().getAttribute("userInfo");
        if(member != null) {
        	member = userService.selectByUserId(member.getUserid());
        	boolean isAdmin = appConfig.getAdminIds().contains(member.getUserid());
        	member.setAdmin(isAdmin);
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
    		result.put("member", member);
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
        	//request.getSession().setAttribute("openId", memberT.getWechatid());
        }
        result.put("status", "success");
		return result;
	}
    
    @RequestMapping(value = "/checkLogin")
	public ModelAndView checkLogin(HttpServletRequest request) {
		ModelAndView mv = null;
		TMembers memberT = (TMembers) request.getSession().getAttribute("userInfo");
		if (memberT != null) {
			mv = new ModelAndView("blank.html");
		} else {
			mv = new ModelAndView("dologin.html");
		}
		return mv;
	}
	
}
