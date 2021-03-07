package com.yimeng.entity;

import java.io.Serializable;

/**
 * Created by user on 2018/6/20.
 * 查询银行卡
 */

public class ModelMemberBankcard implements Serializable {

    private  String bankcardNo;// 银行卡编号（类似主键）
    private  String bankName; // 银行名
    private  String bankcardNum; // 卡号
    private String bankcardName; // 持卡人

    public ModelMemberBankcard(String bankcardNo, String bankName, String bankcardNum, String bankcardName) {
        this.bankcardNo = bankcardNo;
        this.bankName = bankName;
        this.bankcardNum = bankcardNum;
        this.bankcardName = bankcardName;
    }

    public String getBankcardNo() {
        return bankcardNo;
    }

    public void setBankcardNo(String bankcardNo) {
        this.bankcardNo = bankcardNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankcardNum() {
        return bankcardNum;
    }

    public void setBankcardNum(String bankcardNum) {
        this.bankcardNum = bankcardNum;
    }

    public String getBankcardName() {
        return bankcardName;
    }

    public void setBankcardName(String bankcardName) {
        this.bankcardName = bankcardName;
    }
}
