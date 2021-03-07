package com.yimeng.entity;

import java.io.Serializable;

/**
 * 提现记录
 *
 * @author xp
 * @describe 提现记录.
 * @date 2018/6/8.
 */

public class ModelWithdrawDeposit implements Serializable {

    private String accountSource;
    private String withdrawApplyNo;// 提现申请编号
    private String withdrawState;// 提取状态
    private String applyAmt;// 提现申请金额
    private String cardNo;// 卡号
    private String bank;// 银行
    private String withdrawApplyType; //提现渠道
    private String createTime;// 申请时间
    private int isReject;
    private String remark;
    private String amt;//充值记录金额
    private String accountType;//
    private boolean isExpand = false; //是否展开详情
    private String originalPrice;// 商品价格
    private String vipPrice; //会员价
    private String platformAmt;//平台收益

    public ModelWithdrawDeposit(String withdrawApplyNo, String withdrawState, String applyAmt, String cardNo, String bank, String createTime) {
        this.withdrawApplyNo = withdrawApplyNo;
        this.withdrawState = withdrawState;
        this.applyAmt = applyAmt;
        this.cardNo = cardNo;
        this.bank = bank;
        this.createTime = createTime;
    }

    public String getWithdrawApplyNo() {
        return withdrawApplyNo;
    }

    public void setWithdrawApplyNo(String withdrawApplyNo) {
        this.withdrawApplyNo = withdrawApplyNo;
    }

    public String getWithdrawState() {
        return withdrawState;
    }

    public void setWithdrawState(String withdrawState) {
        this.withdrawState = withdrawState;
    }

    public String getApplyAmt() {
        return applyAmt;
    }

    public void setApplyAmt(String applyAmt) {
        this.applyAmt = applyAmt;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsReject() {
        return isReject;
    }

    public void setIsReject(int isReject) {
        this.isReject = isReject;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAccountSource() {
        return accountSource == null ? "" : accountSource;
    }

    public void setAccountSource(String accountSource) {
        this.accountSource = accountSource;
    }

    public String getAmt() {
        return amt == null ? "" : amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getWithdrawApplyType() {
        return withdrawApplyType == null ? "" : withdrawApplyType;
    }

    public void setWithdrawApplyType(String withdrawApplyType) {
        this.withdrawApplyType = withdrawApplyType;
    }

    public String getAccountType() {
        return accountType == null ? "" : accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getOriginalPrice() {
        return originalPrice == null ? "" : originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getVipPrice() {
        return vipPrice == null ? "" : vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

    public String getPlatformAmt() {
        return platformAmt == null ? "" : platformAmt;
    }

    public void setPlatformAmt(String platformAmt) {
        this.platformAmt = platformAmt;
    }
}
