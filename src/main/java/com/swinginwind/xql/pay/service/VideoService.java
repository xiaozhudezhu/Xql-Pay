package com.swinginwind.xql.pay.service;

import java.util.List;
import java.util.Map;

import com.swinginwind.xql.pay.entity.VideoFile;
import com.swinginwind.xql.pay.entity.VideoPermission;
import com.swinginwind.xql.pay.entity.VideoPermissionForm;
import com.swinginwind.xql.pay.entity.VideoType;

public interface VideoService {
	
	List<VideoType> selectVideoTypeByPid(Integer pid);
	
	List<VideoFile> selectVideoFileByPid(Integer pid);
	
	VideoFile selectVideoFileById(Integer id);
	
	List<VideoFile> selectVideoFileByPidPermitted(Integer pid, Integer userId);

	boolean isVideoFilePermitted(Integer id, Integer userId);
	
	List<Map<String, Object>> selectVideoTree();
	
	List<VideoPermission> selectVideoPermissionByUserId(Integer userId);
	
	int submitVideoPermission(VideoPermissionForm form);

	List<VideoType> selectVideoTypeAll();


}
