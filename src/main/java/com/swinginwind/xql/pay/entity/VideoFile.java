package com.swinginwind.xql.pay.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VideoFile {
    /**
     * 
     * 表 : video_file
     * 对应字段 : id
     */
    private Integer id;

    /**
     * 
     * 表 : video_file
     * 对应字段 : name
     */
    private String name;

    /**
     * 
     * 表 : video_file
     * 对应字段 : remark
     */
    private String remark;

    /**
     * 
     * 表 : video_file
     * 对应字段 : pid
     */
    private Integer pid;

    /**
     * 
     * 表 : video_file
     * 对应字段 : file_id
     */
    private String fileId;

    /**
     * 
     * 表 : video_file
     * 对应字段 : sort_code
     */
    private Byte sortCode;
    
    private String free;

    /**
     * 
     * 表 : video_file
     * 对应字段 : create_time
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    /**
     * 
     * 表 : video_file
     * 对应字段 : create_user
     */
    private String createUser;
    
    private String permitted;
    
    private String screenshotContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
    }

    public Byte getSortCode() {
        return sortCode;
    }

    public void setSortCode(Byte sortCode) {
        this.sortCode = sortCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

	public String getPermitted() {
		return permitted;
	}

	public void setPermitted(String permitted) {
		this.permitted = permitted;
	}

	public String getFree() {
		return free;
	}

	public void setFree(String free) {
		this.free = free;
	}

	public String getScreenshotContent() {
		return screenshotContent;
	}

	public void setScreenshotContent(String screenshotContent) {
		this.screenshotContent = screenshotContent;
	}
}