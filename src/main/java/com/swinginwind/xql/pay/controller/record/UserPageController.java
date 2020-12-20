package com.swinginwind.xql.pay.controller.record;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.swinginwind.core.sms.SDKSendTemplateSMS;
import com.swinginwind.xql.pay.config.AppConfig;
import com.swinginwind.xql.pay.entity.TMembers;
import com.swinginwind.xql.pay.service.UserService;

@Controller
@RequestMapping("/userPage")
public class UserPageController {
	
	@Value("${sms.templateid}")
	private String smsTemplateId;
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AppConfig appConfig;
	
	@Autowired
	private SDKSendTemplateSMS smsLib;

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
        	member.setPhonenum(null);
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
    
    @RequestMapping("sendVerifyCode")
	@ResponseBody
	public Map<String, Object> sendVerifyCode(String phone, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();

    	if(StringUtils.isEmpty(phone)) {
			result.put("status", "error");
			return result;
		}
    	if(userService.selectByPhone(phone) != null) {
    		result.put("status", "error");
    		result.put("msg", "手机号已被绑定，请输入其他手机号！");
    		return result;
    	}
    	String verifyCode = String
                .valueOf(new Random().nextInt(899999) + 100000);
    	request.getSession().setAttribute("verifyCode", phone + "_" + verifyCode);
    	request.getSession().setAttribute("verifyCodeCreateTime", new Date());

		boolean success = smsLib.sendTemplateSMS(phone,
				smsTemplateId, new String[] { verifyCode, "3" });
		if(success) {
			result.put("status", "success");
		}
		else
			result.put("status", "error");
		return result;
	}
	
	@RequestMapping("checkVerifyCode")
	@ResponseBody
	public Map<String, Object> checkVerifyCode(String code, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(StringUtils.isEmpty(code)) {
			result.put("status", "error");
		}
		boolean success = false;
		String codeStr = (String) request.getSession().getAttribute("verifyCode");
		if(!StringUtils.isEmpty(codeStr)) {			
			String[] codeArr = codeStr.split("_");
			String phone = codeArr[0];
			String verifyCode = codeArr[1];
			if(code.equals(verifyCode)) {
				//注册系统
				success = true;
		    	TMembers memberT = (TMembers) request.getSession().getAttribute("userInfo");
		        if(memberT == null) {
		        	result.put("status", "error");
		        }
		        else {
		        	memberT.setPhonenum(phone);
		        	TMembers tempMemeber = new TMembers();
		        	tempMemeber.setUserid(memberT.getUserid());
		        	tempMemeber.setPhonenum(phone);
		    		userService.saveUserInfo(tempMemeber);
		        }
			}
		}
		if(success) {
			result.put("status", "success");
			request.getSession().removeAttribute("verifyCode");
		}
		else {
			result.put("status", "error");
		}
		return result;
	}
	
}
