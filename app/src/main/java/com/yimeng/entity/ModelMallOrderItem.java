package com.yimeng.entity;

import java.io.Serializable;

/**
 * 商城订单分页列表二级model
 */

public class ModelMallOrderItem implements Serializable {

    private String createTime;//1529913960000
    private String getScore;//2750
    private String id;//2
    private String logicsFee;//0
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
    private String remark;//null
    private String remindAmt;//0
    private String shopName;//"曲速维修1店"
    private String shopNo;//"201806221958472516704"
    private String unit;//单位
    private String updateTime;//1529913960000
    private String payType;//支付方式
    private String balance; // 余额方式,支付的钱

    public ModelMallOrderItem(String createTime, String getScore, String id, String logicsFee, String menuNo, String orderNo, String orderProductNo, String originalPrice, String productColorSize, String productImg, String productName, String productNo, String productNum, String realAmt, String remark, String remindAmt, String shopName, String shopNo, String unit, String updateTime) {
        this.createTime = createTime;
        this.getScore = getScore;
        this.id = id;
        this.logicsFee = logicsFee;
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
        this.remark = remark;
        this.remindAmt = remindAmt;
        this.shopName = shopName;
        this.shopNo = shopNo;
        this.unit = unit;
        this.updateTime = updateTime;
    }

    public String getPayType() {
        return payType == null ? "" : payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getBalance() {
        return balance == null ? "" : balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGetScore() {
        return getScore;
    }

    public void setGetScore(String getScore) {
        this.getScore = getScore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogicsFee() {
        return logicsFee;
    }

    public void setLogicsFee(String logicsFee) {
        this.logicsFee = logicsFee;
    }

    public String getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(String menuNo) {
        this.menuNo = menuNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderProductNo() {
        return orderProductNo;
    }

    public void setOrderProductNo(String orderProductNo) {
        this.orderProductNo = orderProductNo;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getProductColorSize() {
        return productColorSize;
    }

    public void setProductColorSize(String productColorSize) {
        this.productColorSize = productColorSize;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getRealAmt() {
        return realAmt;
    }

    public void setRealAmt(String realAmt) {
        this.realAmt = realAmt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemindAmt() {
        return remindAmt;
    }

    public void setRemindAmt(String remindAmt) {
        this.remindAmt = remindAmt;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
