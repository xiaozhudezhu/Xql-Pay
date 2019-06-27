package com.swinginwind.xql.pay.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.swinginwind.xql.pay.entity.VideoPermission;

public interface VideoPermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VideoPermission record);

    int insertSelective(VideoPermission record);

    VideoPermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VideoPermission record);

    int updateByPrimaryKey(VideoPermission record);
    
    List<VideoPermission> selectByUserId(Integer userId);
    
    int deleteByUserIds(@Param("userIds") List<Integer> userIds);
    
    int insertBatch(@Param("list") List<VideoPermission> list);
}