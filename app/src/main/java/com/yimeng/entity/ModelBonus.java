package com.yimeng.entity;

import java.io.Serializable;

/**
 * Created by user on 2018/6/20.
 * 我的收益统计
 */

public class ModelBonus implements Serializable {

    private String bonusAccountAmt; // 分润账户余额
    private String unSettlement; // 未结算金额
    private String orderAmtDay; // 今日交易金额
    private String orderAmtBouns; // 今日交易分润
    private String orderAmtMy; // 我的区域现金交易分润
    private String orderAmtRec; // 推荐区域现金交易分润
    private String orderAmtFirst; // 一级会员交易分润
    private String orderAmtSecond; // 二级会员交易分润
    private String shopAmt;// 推荐联盟商家现金交易分润

    private String lastOrderAmtMy; // 上月我的区域现金交易分润
    private String lastOrderAmtRec; // 上月推荐区域现金交易分润
    private String lastOrderAmtFirst; // 上月一级会员交易分润
    private String lastOrderAmtSecond; // 上月二级会员交易分润
    private String lastShopAmt;// 上月推荐联盟商家现金交易分润

    public ModelBonus(String bonusAccountAmt, String orderAmtDay, String orderAmtBouns, String orderAmtMy, String orderAmtRec, String orderAmtFirst, String orderAmtSecond) {
        this.bonusAccountAmt = bonusAccountAmt;
        this.orderAmtDay = orderAmtDay;
        this.orderAmtBouns = orderAmtBouns;
        this.orderAmtMy = orderAmtMy;
        this.orderAmtRec = orderAmtRec;
        this.orderAmtFirst = orderAmtFirst;
        this.orderAmtSecond = orderAmtSecond;
    }

    public String getShopAmt() {
        return shopAmt;
    }

    public void setShopAmt(String shopAmt) {
        this.shopAmt = shopAmt;
    }

    public String getBonusAccountAmt() {
        return bonusAccountAmt;
    }

    public String getLastOrderAmtMy() {
        return lastOrderAmtMy;
    }

    public void setLastOrderAmtMy(String lastOrderAmtMy) {
        this.lastOrderAmtMy = lastOrderAmtMy;
    }

    public String getLastOrderAmtRec() {
        return lastOrderAmtRec;
    }

    public void setLastOrderAmtRec(String lastOrderAmtRec) {
        this.lastOrderAmtRec = lastOrderAmtRec;
    }

    public String getLastOrderAmtFirst() {
        return lastOrderAmtFirst;
    }

    public void setLastOrderAmtFirst(String lastOrderAmtFirst) {
        this.lastOrderAmtFirst = lastOrderAmtFirst;
    }

    public String getLastOrderAmtSecond() {
        return lastOrderAmtSecond;
    }

    public void setLastOrderAmtSecond(String lastOrderAmtSecond) {
        this.lastOrderAmtSecond = lastOrderAmtSecond;
    }

    public String getLastShopAmt() {
        return lastShopAmt;
    }

    public void setLastShopAmt(String lastShopAmt) {
        this.lastShopAmt = lastShopAmt;
    }

    public void setBonusAccountAmt(String bonusAccountAmt) {
        this.bonusAccountAmt = bonusAccountAmt;
    }

    public String getOrderAmtDay() {
        return orderAmtDay;
    }

    public void setOrderAmtDay(String orderAmtDay) {
        this.orderAmtDay = orderAmtDay;
    }

    public String getOrderAmtBouns() {
        return orderAmtBouns;
    }

    public void setOrderAmtBouns(String orderAmtBouns) {
        this.orderAmtBouns = orderAmtBouns;
    }

    public String getOrderAmtMy() {
        return orderAmtMy;
    }

    public void setOrderAmtMy(String orderAmtMy) {
        this.orderAmtMy = orderAmtMy;
    }

    public String getOrderAmtRec() {
        return orderAmtRec;
    }

    public void setOrderAmtRec(String orderAmtRec) {
        this.orderAmtRec = orderAmtRec;
    }

    public String getOrderAmtFirst() {
        return orderAmtFirst;
    }

    public void setOrderAmtFirst(String orderAmtFirst) {
        this.orderAmtFirst = orderAmtFirst;
    }

    public String getOrderAmtSecond() {
        return orderAmtSecond;
    }

    public void setOrderAmtSecond(String orderAmtSecond) {
        this.orderAmtSecond = orderAmtSecond;
    }

    public String getUnSettlement() {
        return unSettlement;
    }

    public void setUnSettlement(String unSettlement) {
        this.unSettlement = unSettlement;
    }
}

