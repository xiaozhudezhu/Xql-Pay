package com.swinginwind.xql.pay.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.swinginwind.xql.pay.entity.ExTempData;
import com.swinginwind.xql.pay.entity.LoginRecord;
import com.swinginwind.xql.pay.entity.TMembers;
import com.swinginwind.xql.pay.mapper.ExTempDataMapper;
import com.swinginwind.xql.pay.mapper.LoginRecordMapper;
import com.swinginwind.xql.pay.mapper.TMembersMapper;
import com.swinginwind.xql.pay.service.UserService;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
    private TMembersMapper tMembersMapper;
	
    @Autowired
    private LoginRecordMapper loginRecordMapper;
    
    @Autowired
    private ExTempDataMapper exTempDataMapper;

	@Override
	public TMembers wechatLogin(WxMpUser wxUser) {
		TMembers member = tMembersMapper.selectByWechatId(wxUser.getOpenId());
		if(member == null) {
			member = new TMembers();
			member.setName(wxUser.getNickname());
			member.setWechatid(wxUser.getOpenId());
			member.setCountry(wxUser.getCountry());
			member.setProvince(wxUser.getProvince());
			member.setCity(wxUser.getProvince() + "/" + wxUser.getCity());
			member.setGender(wxUser.getSexId() == 1 ? "m" : "f");
			member.setLanguage(wxUser.getLanguage());
			member.setHeadimgurl(wxUser.getHeadImgUrl());
			member.setHourspractice("");
			member.setId("");
			member.setPassword("");
			member.setPhonenum("");
			member.setTokencode(UUID.randomUUID().toString().replaceAll("-", ""));
			member.setUserstatus("Y");
			member.setLevel("");
			member.setYearsplay("");
			member.setAge("");
			member.setAchivinglevel("");
			member.setCreatetime(new Date());
			tMembersMapper.insert(member);
		}
		LoginRecord record = new LoginRecord(wxUser);
        loginRecordMapper.insert(record);
        ExTempData tempData = new ExTempData();
        tempData.setToken(UUID.randomUUID().toString().replaceAll("-", ""));
        tempData.setData(JSON.toJSONString(member));
        tempData.setType("login");
        tempData.setCreateTime(new Date());
        member.setLoginToken(tempData.getToken());
        exTempDataMapper.insert(tempData);
		return member;
	}
	
	@Override
	public TMembers selectByUserId(int userId) {
		return tMembersMapper.selectByPrimaryKey(userId);
	}
	
	@Override
	public int saveUserInfo(TMembers member) {
		return tMembersMapper.updateByPrimaryKeySelective(member);
	}
	

}
