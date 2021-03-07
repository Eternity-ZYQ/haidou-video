package com.yimeng.entity;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * 交易统计
 *
 * @author xp
 * @describe 交易统计.
 * @date 2018/6/21.
 */

public class ModelTrdeStatistic implements Serializable {
    private String monthOrderCount;//当天订单笔数
    private String monthEarn;//当天收入
    private String totalOrderCount;//总订单笔数
    private String totalEarn;//总收入
    private LinkedList<BusinessModel> mBusinessList;// 订单数据

    public ModelTrdeStatistic(String monthOrderCount, String monthEarn, String totalOrderCount, String totalEarn, LinkedList<BusinessModel> mBusinessList) {
        this.monthOrderCount = monthOrderCount;
        this.monthEarn = monthEarn;
        this.totalOrderCount = totalOrderCount;
        this.totalEarn = totalEarn;
        this.mBusinessList = mBusinessList;
    }

    public String getMonthOrderCount() {
        return monthOrderCount;
    }

    public void setMonthOrderCount(String monthOrderCount) {
        this.monthOrderCount = monthOrderCount;
    }

    public String getMonthEarn() {
        return monthEarn;
    }

    public void setMonthEarn(String monthEarn) {
        this.monthEarn = monthEarn;
    }

    public String getTotalOrderCount() {
        return TextUtils.isEmpty(totalOrderCount) ? "0" : totalOrderCount;
    }

    public void setTotalOrderCount(String totalOrderCount) {
        this.totalOrderCount = totalOrderCount;
    }

    public String getTotalEarn() {
        return totalEarn;
    }

    public void setTotalEarn(String totalEarn) {
        this.totalEarn = totalEarn;
    }

    public LinkedList<BusinessModel> getmBusinessList() {
        return mBusinessList;
    }

    public void setmBusinessList(LinkedList<BusinessModel> mBusinessList) {
        this.mBusinessList = mBusinessList;
    }
}
