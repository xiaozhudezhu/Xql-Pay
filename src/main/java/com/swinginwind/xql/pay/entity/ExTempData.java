package com.swinginwind.xql.pay.entity;

import java.util.Date;

public class ExTempData {
    /**
     * 
     * �� : ex_temp_data
     * ��Ӧ�ֶ� : token
     */
    private String token;

    /**
     * 
     * �� : ex_temp_data
     * ��Ӧ�ֶ� : type
     */
    private String type;

    /**
     * 
     * �� : ex_temp_data
     * ��Ӧ�ֶ� : create_time
     */
    private Date createTime;

    /**
     * 
     * �� : ex_temp_data
     * ��Ӧ�ֶ� : data
     */
    private String data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data == null ? null : data.trim();
    }
}