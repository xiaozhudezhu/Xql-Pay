package com.swinginwind.xql.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

public class BaseOrder {
    /**
     * 
     * 表 : base_order
     * 对应字段 : id
     */
    private Integer id;

    /**
     * 
     * 表 : base_order
     * 对应字段 : order_user
     */
    private String orderUser;

    /**
     * 
     * 表 : base_order
     * 对应字段 : order_time
     */
    private Date orderTime;

    /**
     * 
     * 表 : base_order
     * 对应字段 : pay_user
     */
    private String payUser;

    /**
     * 
     * 表 : base_order
     * 对应字段 : pay_time
     */
    private Date payTime;

    /**
     * 
     * 表 : base_order
     * 对应字段 : amount
     */
    private BigDecimal amount;

    /**
     * 
     * 表 : base_order
     * 对应字段 : status
     */
    private Byte status;

    /**
     * 
     * 表 : base_order
     * 对应字段 : order_content
     */
    private String orderContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser == null ? null : orderUser.trim();
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getPayUser() {
        return payUser;
    }

    public void setPayUser(String payUser) {
        this.payUser = payUser == null ? null : payUser.trim();
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent == null ? null : orderContent.trim();
    }
}