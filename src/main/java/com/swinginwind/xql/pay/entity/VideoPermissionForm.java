package com.swinginwind.xql.pay.entity;

import java.util.Date;
import java.util.List;

public class VideoPermissionForm {
	
	private int configType;
	
	private Date startDate;
	
	private Date dueDate;
	
	private List<Integer> userIdList;
	
	private List<VideoPermission> permissionList;

	public int getConfigType() {
		return configType;
	}

	public void setConfigType(int configType) {
		this.configType = configType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public List<Integer> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<Integer> userIdList) {
		this.userIdList = userIdList;
	}

	public List<VideoPermission> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<VideoPermission> permissionList) {
		this.permissionList = permissionList;
	}

}
