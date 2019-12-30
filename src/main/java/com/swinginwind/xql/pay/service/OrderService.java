package com.swinginwind.xql.pay.service;

import java.util.Map;

import com.swinginwind.xql.pay.entity.BaseOrder;

public interface OrderService {
	
	int deleteByPrimaryKey(Integer id);

    int insert(BaseOrder record);

    int insertSelective(BaseOrder record);

    BaseOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseOrder record);

    int updateByPrimaryKeyWithBLOBs(BaseOrder record);

    int updateByPrimaryKey(BaseOrder record);
    
    boolean wxPayNotify(Map<String, String> params);

}
