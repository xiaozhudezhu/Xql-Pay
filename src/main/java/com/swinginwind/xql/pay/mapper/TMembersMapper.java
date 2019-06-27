package com.swinginwind.xql.pay.mapper;

import java.util.List;
import java.util.Map;

import com.swinginwind.xql.pay.entity.TMembers;

public interface TMembersMapper {
    int deleteByPrimaryKey(Integer userid);

    int insert(TMembers record);

    int insertSelective(TMembers record);

    TMembers selectByPrimaryKey(Integer userid);

    int updateByPrimaryKeySelective(TMembers record);

    int updateByPrimaryKey(TMembers record);
    
    TMembers selectByWechatId(String wechatid);
    
    List<TMembers> select(TMembers param);
    
}