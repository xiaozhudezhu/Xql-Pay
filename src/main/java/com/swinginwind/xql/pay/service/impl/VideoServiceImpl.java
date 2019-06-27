package com.swinginwind.xql.pay.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swinginwind.xql.pay.entity.VideoFile;
import com.swinginwind.xql.pay.entity.VideoPermission;
import com.swinginwind.xql.pay.entity.VideoPermissionForm;
import com.swinginwind.xql.pay.entity.VideoType;
import com.swinginwind.xql.pay.mapper.VideoFileMapper;
import com.swinginwind.xql.pay.mapper.VideoPermissionMapper;
import com.swinginwind.xql.pay.mapper.VideoTypeMapper;
import com.swinginwind.xql.pay.service.VideoService;

@Service
public class VideoServiceImpl implements VideoService {
	
	@Autowired
	VideoTypeMapper videoTypeMapper;
	@Autowired
	VideoFileMapper videoFileMapper;
	@Autowired
	VideoPermissionMapper videoPermissionMapper;

	@Override
	public List<VideoType> selectVideoTypeByPid(Integer pid) {
		return videoTypeMapper.selectByPid(pid);
	}
	
	@Override
	public List<VideoType> selectVideoTypeAll() {
		return videoTypeMapper.selectAll();
	}

	@Override
	public List<VideoFile> selectVideoFileByPid(Integer pid) {
		return videoFileMapper.selectByPid(pid);
	}

	@Override
	public VideoFile selectVideoFileById(Integer id) {
		return videoFileMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<VideoFile> selectVideoFileByPidPermitted(Integer pid, Integer userId) {
		return videoFileMapper.selectByPidPermitted(pid, userId);
	}
	
	@Override
	public boolean isVideoFilePermitted(Integer id, Integer userId) {
		return "1".equals(videoFileMapper.isVideoFilePermitted(id, userId));
	}

	@Override
	public List<Map<String, Object>> selectVideoTree() {
		return videoFileMapper.selectVideoTree();
	}

	@Override
	public List<VideoPermission> selectVideoPermissionByUserId(Integer userId) {
		return videoPermissionMapper.selectByUserId(userId);
	}

	@Override
	public int submitVideoPermission(VideoPermissionForm form) {
		int count = 0;
		videoPermissionMapper.deleteByUserIds(form.getUserIdList());
		for(int userId : form.getUserIdList()) {
			for(VideoPermission permission : form.getPermissionList()) {
				permission.setUserId(userId);
				if(form.getConfigType() == 2) {
					permission.setStartDate(form.getStartDate());
					permission.setDueDate(form.getDueDate());
				}
			}
			count += videoPermissionMapper.insertBatch(form.getPermissionList());
		}
		return count;
	}

}
