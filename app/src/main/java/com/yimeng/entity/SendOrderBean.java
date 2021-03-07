package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/5 11:41 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class SendOrderBean {


    /**
     * orderNo : OR2019070510384344600000001
     * orderName : 法国洋酒XO白兰地3支组合装
     * productImg : /fileupload/shopImage/20190612/1560321733175914501881(350x350).jpg
     * productNum : 1
     * memberNo : null
     * nickname : 合伙人8
     * mobileNo : 14500008888
     * originalPrice : 20000
     * realAmt : 0
     * dueAmt : 20000
     * orderType : null
     * orderStatus : null
     * payType : yu_e
     * createTime : 1562294323000
     * updateTime : null
     */

    private String orderNo;
    private String orderName;
    private String productImg;
    private int productNum;
    private String memberNo;
    private String nickname;
    private String mobileNo;
    private int originalPrice;
    private int realAmt;
    private String dueAmt;
    private String orderType;
    private String orderStatus;
    private String payType;
    private boolean isChecked;

    public String getOrderNo() {
        return orderNo == null ? "" : orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderName() {
        return orderName == null ? "" : orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getProductImg() {
        return productImg == null ? "" : productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public String getMemberNo() {
        return memberNo == null ? "" : memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobileNo() {
        return mobileNo == null ? "" : mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getRealAmt() {
        return realAmt;
    }

    public void setRealAmt(int realAmt) {
        this.realAmt = realAmt;
    }

    public String getDueAmt() {
        return dueAmt == null ? "" : dueAmt;
    }

    public void setDueAmt(String dueAmt) {
        this.dueAmt = dueAmt;
    }

    public String getOrderType() {
        return orderType == null ? "" : orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderStatus() {
        return orderStatus == null ? "" : orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPayType() {
        return payType == null ? "" : payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
