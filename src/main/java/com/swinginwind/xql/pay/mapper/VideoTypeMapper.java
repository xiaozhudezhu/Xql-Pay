package com.swinginwind.xql.pay.mapper;

import java.util.List;

import com.swinginwind.xql.pay.entity.VideoType;

public interface VideoTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VideoType record);

    int insertSelective(VideoType record);

    VideoType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VideoType record);

    int updateByPrimaryKey(VideoType record);
    
    List<VideoType> selectByPid(Integer pid);
    
    List<VideoType> selectAll();
}