package com.swinginwind.xql.pay.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.swinginwind.xql.pay.entity.VideoFile;

public interface VideoFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VideoFile record);

    int insertSelective(VideoFile record);

    VideoFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VideoFile record);

    int updateByPrimaryKey(VideoFile record);
    
    List<VideoFile> selectByPid(Integer pid);
    
    List<VideoFile> selectByPidPermitted(@Param("pid") Integer pid, @Param("userId") Integer userId);
    
    String isVideoFilePermitted(@Param("id") Integer id, @Param("userId") Integer userId);
    
    List<Map<String, Object>> selectVideoTree();

}