package com.swinginwind.xql.pay.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.swinginwind.xql.pay.entity.ExTempData;
import com.swinginwind.xql.pay.entity.LoginRecord;
import com.swinginwind.xql.pay.entity.TMembers;
import com.swinginwind.xql.pay.mapper.ExTempDataMapper;
import com.swinginwind.xql.pay.mapper.LoginRecordMapper;
import com.swinginwind.xql.pay.mapper.TMembersMapper;
import com.swinginwind.xql.pay.service.UserService;
import com.swinginwind.xql.wechat.utils.EmojiFilter;

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
		System.out.println(wxUser.getNickname());
		//解决表情符号的问题
		if(!StringUtils.isEmpty(wxUser.getNickname()))
			wxUser.setNickname(EmojiFilter.filterEmoji(wxUser.getNickname()));
		System.out.println("###Get UnionId: " + wxUser.getUnionId());
		TMembers member = tMembersMapper.selectByWechatId(wxUser.getUnionId());
		if (member == null) {
			member = new TMembers();
			member.setName(wxUser.getNickname());
			member.setWechatid(wxUser.getUnionId());
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
			member.setUserstatus("N");
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
		if(!StringUtils.isEmpty(member.getName()))
			member.setName(EmojiFilter.filterEmoji(member.getName()));
		if (!StringUtils.isEmpty(member.getAchivinglevel()) && !StringUtils.isEmpty(member.getCity())
				&& !StringUtils.isEmpty(member.getGender()) && !StringUtils.isEmpty(member.getHourspractice())
				&& /*!StringUtils.isEmpty(member.getId()) &&*/ !StringUtils.isEmpty(member.getLevel())
				&& !StringUtils.isEmpty(member.getYearsplay()))
			member.setUserstatus("Y");
		else
			member.setUserstatus("N");
		return tMembersMapper.updateByPrimaryKeySelective(member);
	}

	@Override
	public List<TMembers> select(TMembers param) {
		return tMembersMapper.select(param);
	}

}
