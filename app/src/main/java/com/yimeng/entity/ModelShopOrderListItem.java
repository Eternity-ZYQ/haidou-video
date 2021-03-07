package com.yimeng.entity;

import java.io.Serializable;

/**
 * 店铺订单信息
 */
public class ModelShopOrderListItem implements Serializable {

    private String menuNo;//"201806251539507099183"
    private String orderNo;//"OR2018062516060056100000002"
    private String orderProductNo;//"201806251606005782082"
    private String originalPrice;//5500
    private String productColorSize;// 商品型号颜色
    private String productImg;//商品图片
    private String productName;//"商品000006"
    private String productNo;//"201806251552102727525"
    private String productNum;//1
    private String realAmt;//5500
    private String remindAmt;//0
    private String shopNo;

    public ModelShopOrderListItem(String menuNo, String orderNo, String orderProductNo, String originalPrice, String productColorSize, String productImg, String productName, String productNo, String productNum, String realAmt, String remindAmt) {
        this.menuNo = menuNo;
        this.orderNo = orderNo;
        this.orderProductNo = orderProductNo;
        this.originalPrice = originalPrice;
        this.productColorSize = productColorSize;
        this.productImg = productImg;
        this.productName = productName;
        this.productNo = productNo;
        this.productNum = productNum;
        this.realAmt = realAmt;
        this.remindAmt = remindAmt;
    }

    public String getShopNo() {
        return shopNo == null ? "" : shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getMenuNo() {
        return menuNo == null ? "" : menuNo;
    }

    public void setMenuNo(String menuNo) {
        this.menuNo = menuNo;
    }

    public String getOrderNo() {
        return orderNo == null ? "" : orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderProductNo() {
        return orderProductNo == null ? "" : orderProductNo;
    }

    public void setOrderProductNo(String orderProductNo) {
        this.orderProductNo = orderProductNo;
    }

    public String getOriginalPrice() {
        return originalPrice == null ? "" : originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getProductColorSize() {
        return productColorSize == null ? "" : productColorSize;
    }

    public void setProductColorSize(String productColorSize) {
        this.productColorSize = productColorSize;
    }

    public String getProductImg() {
        return productImg == null ? "" : productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductName() {
        return productName == null ? "" : productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNo() {
        return productNo == null ? "" : productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductNum() {
        return productNum == null ? "" : productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getRealAmt() {
        return realAmt == null ? "" : realAmt;
    }

    public void setRealAmt(String realAmt) {
        this.realAmt = realAmt;
    }

    public String getRemindAmt() {
        return remindAmt == null ? "" : remindAmt;
    }

    public void setRemindAmt(String remindAmt) {
        this.remindAmt = remindAmt;
    }
}
