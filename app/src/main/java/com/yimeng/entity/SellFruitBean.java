package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/23 7:00 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 在售活跃果子
 * </pre>
 */
public class SellFruitBean {

    // 价格区块编号
    private String blockNo;
    // 任务模块编号
    private String merchantTaskNo;
    // 任务名称
    private String merchantTaskName;
    // 任务类型，rec：推荐任务
    private String merchantTaskCode;
    // 任务编号
    private String memberTaskNo;
    // 我的活跃果数量
    private int fruitCount;
    // 等级
    private int rlevel;
    // 等级名称
    private String recTitle;
    // 挂卖人编号
    private String tradeFrom;

    // 活跃果编号
    private String fruitTradeNo;
    // 活跃果价格(分)
    private String tradeFromAmt;
    // 在售活跃果数量
    private int tradeNum;

    public String getMerchantTaskName() {
        return merchantTaskName == null ? "" : merchantTaskName;
    }

    public void setMerchantTaskName(String merchantTaskName) {
        this.merchantTaskName = merchantTaskName;
    }

    public String getMerchantTaskCode() {
        return merchantTaskCode == null ? "" : merchantTaskCode;
    }

    public void setMerchantTaskCode(String merchantTaskCode) {
        this.merchantTaskCode = merchantTaskCode;
    }

    public String getFruitTradeNo() {
        return fruitTradeNo == null ? "" : fruitTradeNo;
    }

    public void setFruitTradeNo(String fruitTradeNo) {
        this.fruitTradeNo = fruitTradeNo;
    }

    public String getTradeFromAmt() {
        return tradeFromAmt == null ? "" : tradeFromAmt;
    }

    public void setTradeFromAmt(String tradeFromAmt) {
        this.tradeFromAmt = tradeFromAmt;
    }

    public int getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(int tradeNum) {
        this.tradeNum = tradeNum;
    }

    public String getBlockNo() {
        return blockNo == null ? "" : blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public String getMerchantTaskNo() {
        return merchantTaskNo == null ? "" : merchantTaskNo;
    }

    public void setMerchantTaskNo(String merchantTaskNo) {
        this.merchantTaskNo = merchantTaskNo;
    }

    public String getMemberTaskNo() {
        return memberTaskNo == null ? "" : memberTaskNo;
    }

    public void setMemberTaskNo(String memberTaskNo) {
        this.memberTaskNo = memberTaskNo;
    }

    public String getTradeFrom() {
        return tradeFrom == null ? "" : tradeFrom;
    }

    public void setTradeFrom(String tradeFrom) {
        this.tradeFrom = tradeFrom;
    }

    public int getFruitCount() {
        return fruitCount;
    }

    public void setFruitCount(int fruitCount) {
        this.fruitCount = fruitCount;
    }

    public int getRlevel() {
        return rlevel;
    }

    public void setRlevel(int rlevel) {
        this.rlevel = rlevel;
    }

    public String getRecTitle() {
        return recTitle == null ? "" : recTitle;
    }

    public void setRecTitle(String recTitle) {
        this.recTitle = recTitle;
    }
}
