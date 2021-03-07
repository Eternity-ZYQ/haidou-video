package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/12/6 0006 上午 11:11.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class SwipeLogDateBean {

    /**
     * 时间
     */
    private String time;
    /**
     * 刷卡金额
     */
    private double swipeMoney;
    /**
     * 实际到账金额
     */
    private double accountMoney;

    public SwipeLogDateBean(String time, double swipeMoney, double accountMoney) {
        this.time = time;
        this.swipeMoney = swipeMoney;
        this.accountMoney = accountMoney;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMoney(double swipeMoney, double accountMoney){
        this.swipeMoney = swipeMoney;
        this.accountMoney = accountMoney;
    }

    public double getSwipeMoney() {
        return swipeMoney;
    }

    public void setSwipeMoney(double swipeMoney) {
        this.swipeMoney = swipeMoney;
    }

    public double getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(double accountMoney) {
        this.accountMoney = accountMoney;
    }
}
