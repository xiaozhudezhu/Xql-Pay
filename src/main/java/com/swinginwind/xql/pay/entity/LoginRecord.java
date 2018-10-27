package com.swinginwind.xql.pay.entity;


import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

public class LoginRecord extends WxMpUser {
	
	public LoginRecord(WxMpUser user) {
		BeanUtils.copyProperties(user, this);
		this.setLoginTime(new Date());
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date loginTime;

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

}
