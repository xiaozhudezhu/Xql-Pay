package com.swinginwind.xql.pay.mapper;

import com.swinginwind.xql.pay.entity.TMembers;

public interface TMembersMapper {
    int deleteByPrimaryKey(Integer userid);

    int insert(TMembers record);

    int insertSelective(TMembers record);

    TMembers selectByPrimaryKey(Integer userid);

    int updateByPrimaryKeySelective(TMembers record);

    int updateByPrimaryKey(TMembers record);
    
    TMembers selectByWechatId(String wechatid);
}