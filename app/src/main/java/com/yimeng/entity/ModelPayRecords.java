package com.yimeng.entity;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * 充值记录
 *
 * @author xp
 * @describe 充值记录.
 * @date 2018/6/8.
 */

public class ModelPayRecords implements Serializable {

    private String id;
    private String title;
    private String date;
    private String money;// 当前金额
    private String total;// 总计金额
    private LinkedList<ModelPayRecords> mRecordList;// 记录
    // 统计信息
    private String amtType;// 不是充值时为空
    private String totalRechargeMoney;// 总充值金额
    private String totalRechargeCount;// 总充值笔数
    private String remark;

    public ModelPayRecords() {
    }

    public ModelPayRecords(String id, String title, String date, String money, String total) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.money = money;
        this.total = total;
    }

    public String getAmtType() {
        return amtType;
    }

    public void setAmtType(String amtType) {
        this.amtType = amtType;
    }

    public String getTotalRechargeMoney() {
        return totalRechargeMoney;
    }

    public void setTotalRechargeMoney(String totalRechargeMoney) {
        this.totalRechargeMoney = totalRechargeMoney;
    }

    public String getTotalRechargeCount() {
        return totalRechargeCount;
    }

    public void setTotalRechargeCount(String totalRechargeCount) {
        this.totalRechargeCount = totalRechargeCount;
    }

    public LinkedList<ModelPayRecords> getmRecordList() {
        return mRecordList;
    }

    public void setmRecordList(LinkedList<ModelPayRecords> mRecordList) {
        this.mRecordList = mRecordList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
