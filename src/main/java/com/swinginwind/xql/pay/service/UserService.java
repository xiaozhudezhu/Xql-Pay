package com.swinginwind.xql.pay.service;

import java.util.List;

import com.swinginwind.xql.pay.entity.TMembers;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

public interface UserService {
	
	TMembers wechatLogin(WxMpUser wxUser);

	TMembers selectByUserId(int userId);

	int saveUserInfo(TMembers member);
	
	List<TMembers> select(TMembers param);

	TMembers selectByPhone(String phone);

}
