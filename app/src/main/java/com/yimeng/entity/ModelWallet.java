package com.yimeng.entity;

import java.io.Serializable;

/**
 * 钱包信息
 *
 * @author xp
 * @describe 钱包信息.
 * @date 2018/6/25.
 */

public class ModelWallet implements Serializable {

    private String balance;//站内余额
    private String score;//现金额度
    private String balanceAmtFreeze;//收益余额
    private String baodanbi;//支付宝余额
    private String yongjin;//微信余额
    private String cycleLoginTimes;// 提现额度

    public ModelWallet(String balance, String score, String balanceAmtFreeze, String baodanbi, String cycleLoginTimes) {
        this.balance = balance;
        this.score = score;
        this.balanceAmtFreeze = balanceAmtFreeze;
        this.baodanbi = baodanbi;
        this.cycleLoginTimes = cycleLoginTimes;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getBalanceAmtFreeze() {
        return balanceAmtFreeze;
    }

    public void setBalanceAmtFreeze(String balanceAmtFreeze) {
        this.balanceAmtFreeze = balanceAmtFreeze;
    }

    public String getBaodanbi() {
        return baodanbi;
    }

    public void setBaodanbi(String baodanbi) {
        this.baodanbi = baodanbi;
    }

    public String getCycleLoginTimes() {
        return cycleLoginTimes == null ? "" : cycleLoginTimes;
    }

    public void setCycleLoginTimes(String cycleLoginTimes) {
        this.cycleLoginTimes = cycleLoginTimes;
    }

    public String getYongjin() {
        return yongjin == null ? "" : yongjin;
    }

    public void setYongjin(String yongjin) {
        this.yongjin = yongjin;
    }
}
