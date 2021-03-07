package com.yimeng.entity;

import java.io.Serializable;

/**
 * Created by user on 2018/6/20.
 * 交易报表
 */

public class ModelOrderReport implements Serializable {

    private String dateT; // MM-dd格式的时间
    private String dateStr;//yyyy-MM-dd格式的时间
    private String orderCount;//订单数量
    private String scoreAmtSum;//实际收入
    private String refundCount;//退款数量
    private String refundAmtSum;//退款金额

    public ModelOrderReport(String dateT, String dateStr, String orderCount, String scoreAmtSum, String refundCount, String refundAmtSum) {
        this.dateT = dateT;
        this.dateStr = dateStr;
        this.orderCount = orderCount;
        this.scoreAmtSum = scoreAmtSum;
        this.refundCount = refundCount;
        this.refundAmtSum = refundAmtSum;
    }

    public String getDateT() {
        return dateT;
    }

    public void setDateT(String dateT) {
        this.dateT = dateT;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getScoreAmtSum() {
        return scoreAmtSum;
    }

    public void setScoreAmtSum(String scoreAmtSum) {
        this.scoreAmtSum = scoreAmtSum;
    }

    public String getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(String refundCount) {
        this.refundCount = refundCount;
    }

    public String getRefundAmtSum() {
        return refundAmtSum;
    }

    public void setRefundAmtSum(String refundAmtSum) {
        this.refundAmtSum = refundAmtSum;
    }
}
