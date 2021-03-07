package com.yimeng.entity;

import com.yimeng.utils.DateUtil;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/14 0014 上午 11:11.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class OrderBean {
    private String orderNo;
    private String orderStatus;
    private String orderStatusName;
    private long createTime;
    private long loanAmt;
    private String productImg;
    private String productName;

    public String getOrderNo() {
        return orderNo == null ? "" : orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderStatus() {
        return orderStatus == null ? "" : orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatusName == null ? "" : orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getCreateTime() {
        return DateUtil.getAssignDate(createTime, 3);
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(long loanAmt) {
        this.loanAmt = loanAmt;
    }

    public String getProductImg() {
        return productImg == null ? "" : productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductName() {
        return productName == null ? "" : productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
