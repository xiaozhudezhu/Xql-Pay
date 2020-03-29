package com.swinginwind.xql.pay.entity;

import java.util.Date;
import java.util.List;

public class VideoPermissionForm {
	
	private Integer operateUserId;
	
	private String operateUserName;
	
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

	/**
	 * @return the operateUserId
	 */
	public Integer getOperateUserId() {
		return operateUserId;
	}

	/**
	 * @param operateUserId the operateUserId to set
	 */
	public void setOperateUserId(Integer operateUserId) {
		this.operateUserId = operateUserId;
	}

	/**
	 * @return the operateUserName
	 */
	public String getOperateUserName() {
		return operateUserName;
	}

	/**
	 * @param operateUserName the operateUserName to set
	 */
	public void setOperateUserName(String operateUserName) {
		this.operateUserName = operateUserName;
	}

}
