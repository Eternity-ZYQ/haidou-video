package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/12 3:23 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 在售种子
 * </pre>
 */
public class SellSeedBean {

    private int id;
    private String seedTradeNo;
    private String tradeType;
    private String tradeFrom;
    private String fromNikename;
    private String formHeadImg;
    private Object tradeTo;
    private Object toNikename;
    private Object toHeadImg;
    private int tradeFromAmt;
    private Object tradeToAmt;
    private int tradeNum;
    private String status;
    private Object remark;
    private Object tradeTime;
    private long createTime;
    private long updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeedTradeNo() {
        return seedTradeNo == null ? "" : seedTradeNo;
    }

    public void setSeedTradeNo(String seedTradeNo) {
        this.seedTradeNo = seedTradeNo;
    }

    public String getTradeType() {
        return tradeType == null ? "" : tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeFrom() {
        return tradeFrom == null ? "" : tradeFrom;
    }

    public void setTradeFrom(String tradeFrom) {
        this.tradeFrom = tradeFrom;
    }

    public String getFromNikename() {
        return fromNikename == null ? "" : fromNikename;
    }

    public void setFromNikename(String fromNikename) {
        this.fromNikename = fromNikename;
    }

    public String getFormHeadImg() {
        return formHeadImg == null ? "" : formHeadImg;
    }

    public void setFormHeadImg(String formHeadImg) {
        this.formHeadImg = formHeadImg;
    }

    public Object getTradeTo() {
        return tradeTo;
    }

    public void setTradeTo(Object tradeTo) {
        this.tradeTo = tradeTo;
    }

    public Object getToNikename() {
        return toNikename;
    }

    public void setToNikename(Object toNikename) {
        this.toNikename = toNikename;
    }

    public Object getToHeadImg() {
        return toHeadImg;
    }

    public void setToHeadImg(Object toHeadImg) {
        this.toHeadImg = toHeadImg;
    }

    public int getTradeFromAmt() {
        return tradeFromAmt;
    }

    public void setTradeFromAmt(int tradeFromAmt) {
        this.tradeFromAmt = tradeFromAmt;
    }

    public Object getTradeToAmt() {
        return tradeToAmt;
    }

    public void setTradeToAmt(Object tradeToAmt) {
        this.tradeToAmt = tradeToAmt;
    }

    public int getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(int tradeNum) {
        this.tradeNum = tradeNum;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public Object getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Object tradeTime) {
        this.tradeTime = tradeTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
