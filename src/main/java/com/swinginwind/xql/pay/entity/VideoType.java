package com.swinginwind.xql.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

public class VideoType {
    /**
     * 
     * 表 : video_type
     * 对应字段 : id
     */
    private Integer id;

    /**
     * 
     * 表 : video_type
     * 对应字段 : name
     */
    private String name;

    /**
     * 
     * 表 : video_type
     * 对应字段 : remark
     */
    private String remark;

    /**
     * 
     * 表 : video_type
     * 对应字段 : pid
     */
    private Integer pid;
    
    private BigDecimal price;

    /**
     * 
     * 表 : video_type
     * 对应字段 : sort_code
     */
    private Byte sortCode;

    /**
     * 
     * 表 : video_type
     * 对应字段 : create_user
     */
    private String createUser;

    /**
     * 
     * 表 : video_type
     * 对应字段 : create_time
     */
    private Date createTime;

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

    public Byte getSortCode() {
        return sortCode;
    }

    public void setSortCode(Byte sortCode) {
        this.sortCode = sortCode;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}