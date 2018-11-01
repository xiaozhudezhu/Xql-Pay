package com.swinginwind.xql.pay.mapper;

import com.swinginwind.xql.pay.entity.ExTempData;

public interface ExTempDataMapper {
    int deleteByPrimaryKey(String token);

    int insert(ExTempData record);

    int insertSelective(ExTempData record);

    ExTempData selectByPrimaryKey(String token);

    int updateByPrimaryKeySelective(ExTempData record);

    int updateByPrimaryKeyWithBLOBs(ExTempData record);

    int updateByPrimaryKey(ExTempData record);
}