package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/12 4:19 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 种子日志
 * </pre>
 */
public class SeedLogBean {


    private String seedNo;
    private String logType;
    private String logName;
    private String logInOut;
    private Object tradeNo;
    private String tradeFrom;
    private Object tradeTo;
    private int tradeAmt;
    private int tradeNum;
    private String status;
    private String remark;
    private long createTime;
    private long updateTime;

    public String getSeedNo() {
        return seedNo == null ? "" : seedNo;
    }

    public void setSeedNo(String seedNo) {
        this.seedNo = seedNo;
    }

    public String getLogType() {
        return logType == null ? "" : logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getLogName() {
        return logName == null ? "" : logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getLogInOut() {
        return logInOut == null ? "" : logInOut;
    }

    public void setLogInOut(String logInOut) {
        this.logInOut = logInOut;
    }

    public Object getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(Object tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTradeFrom() {
        return tradeFrom == null ? "" : tradeFrom;
    }

    public void setTradeFrom(String tradeFrom) {
        this.tradeFrom = tradeFrom;
    }

    public Object getTradeTo() {
        return tradeTo;
    }

    public void setTradeTo(Object tradeTo) {
        this.tradeTo = tradeTo;
    }

    public int getTradeAmt() {
        return tradeAmt;
    }

    public void setTradeAmt(int tradeAmt) {
        this.tradeAmt = tradeAmt;
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

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
