package com.swinginwind.xql.pay.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.swinginwind.xql.pay.entity.LoginRecord;

@Mapper
public interface LoginRecordMapper {
	
	@Insert("insert into login_record(open_id, nickname, sex, language, city, province,country,head_img_url,union_id,login_time) values (#{openId},#{nickname},#{sex},#{language},#{city},#{province},#{country},#{headImgUrl},#{unionId},#{loginTime})")
	void insert(LoginRecord record);

}
