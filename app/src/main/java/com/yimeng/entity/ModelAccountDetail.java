package com.yimeng.entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by user on 2018/6/20.
 * 店铺账户详情
 */

public class ModelAccountDetail implements Serializable {

    private String noSettle;// 未结算金额
    private String yongjin;//店铺余额

    public ModelAccountDetail(String noSettle, String yongjin) {
        this.noSettle = noSettle;
        this.yongjin = yongjin;
    }

    public String getNoSettle() {
        return TextUtils.isEmpty(noSettle) || noSettle == null ? "0" : noSettle;
    }

    public void setNoSettle(String noSettle) {
        this.noSettle = noSettle;
    }

    public String getYongjin() {
        return TextUtils.isEmpty(yongjin) || yongjin == null ? "0" : yongjin;
    }

    public void setYongjin(String yongjin) {
        this.yongjin = yongjin;
    }
}
