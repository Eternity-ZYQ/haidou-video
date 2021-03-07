package com.yimeng.entity;

import java.io.Serializable;

/**
 * 店铺订单商品
 *
 * @author xp
 * @describe 店铺订单商品.
 * @date 2018/7/4.
 */

public class ModelOrderGoods implements Serializable {

    private String orderProductNo;//编号
    private String productName;//名称
    private String realAmt;//金额
    private String productImg;//图片
    private String productNum; //数量
    private int type;// 1商品, 2保修

    public ModelOrderGoods(String orderProductNo, String productName, String realAmt, String productImg, int type) {
        this.orderProductNo = orderProductNo;
        this.productName = productName;
        this.realAmt = realAmt;
        this.productImg = productImg;
        this.type = type;
    }

    public String getOrderProductNo() {
        return orderProductNo;
    }

    public void setOrderProductNo(String orderProductNo) {
        this.orderProductNo = orderProductNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRealAmt() {
        return realAmt;
    }

    public void setRealAmt(String realAmt) {
        this.realAmt = realAmt;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProductNum() {
        return productNum == null ? "" : productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }
}
