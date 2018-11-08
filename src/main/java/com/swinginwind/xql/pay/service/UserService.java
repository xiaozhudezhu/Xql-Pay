package com.swinginwind.xql.pay.service;

import com.swinginwind.xql.pay.entity.TMembers;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

public interface UserService {
	
	TMembers wechatLogin(WxMpUser wxUser);

	TMembers selectByUserId(int userId);

	int saveUserInfo(TMembers member);

}
