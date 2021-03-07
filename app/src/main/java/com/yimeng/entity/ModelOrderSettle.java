package com.yimeng.entity;

import java.io.Serializable;

/**
 * Created by user on 2018/6/20.
 * 入账记录
 */

public class ModelOrderSettle implements Serializable {

    private String orderNo; // 订单编号
    private String orderName; // 订单名称
    private String dueAmt; // 金额
    private String createTime; // 时间

    public ModelOrderSettle(String orderNo, String orderName, String dueAmt, String createTime) {
        this.orderNo = orderNo;
        this.orderName = orderName;
        this.dueAmt = dueAmt;
        this.createTime = createTime;
    }


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getDueAmt() {
        return dueAmt;
    }

    public void setDueAmt(String dueAmt) {
        this.dueAmt = dueAmt;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
