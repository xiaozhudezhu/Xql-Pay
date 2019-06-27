package com.swinginwind.xql.pay.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VideoPermission {
    /**
     * 
     * 表 : video_permission
     * 对应字段 : id
     */
    private Integer id;

    /**
     * 
     * 表 : video_permission
     * 对应字段 : user_id
     */
    private Integer userId;

    /**
     * 
     * 表 : video_permission
     * 对应字段 : video_id
     */
    private Integer videoId;

    /**
     * 
     * 表 : video_permission
     * 对应字段 : start_date
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date startDate;

    /**
     * 
     * 表 : video_permission
     * 对应字段 : due_date
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date dueDate;

    /**
     * 
     * 表 : video_permission
     * 对应字段 : order_id
     */
    private Integer orderId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}