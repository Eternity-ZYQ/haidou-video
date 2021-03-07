package com.yimeng.entity;

import java.io.Serializable;


// TODO 订单详情

/**
 * 订单详情
 *
 * @author xp
 * @describe 订单详情.
 * @date 2018/7/19.
 */

public class ModelOrderDetail implements Serializable {

    private String orderNo;//订单编号
    private String shopNo;//店铺编号
    private String originalPrice;//商品金额
    private String shopLogo;//店铺图片
    private String shopName;//店铺名称
    private String imagesPath;//商品图片
    private String productName;//商品名称
    private String orderName;//订单名称
    private String realAmt;//实付交易金额
    private String orderStatus;//订单状态
    private String payType;//支付类型
    private String payStatus;//支付状态
    private String createTime;//交易时间
    private String score;//补贴麻豆
    private String scoreAmt;//商家获得金额
    private String nickname;//用户昵称
    private String memberName;//用户姓名
    private String leaveMsg;//买家留言
    private String orderStatusStr;
    private String payTypeStr;
    private String dueAmt;//订单金额

    public ModelOrderDetail(String orderNo, String shopNo, String originalPrice, String shopLogo, String shopName, String imagesPath, String productName, String orderName, String realAmt, String orderStatus, String payType, String payStatus, String createTime, String score, String scoreAmt, String nickname, String memberName, String leaveMsg) {
        this.orderNo = orderNo;
        this.shopNo = shopNo;
        this.originalPrice = originalPrice;
        this.shopLogo = shopLogo;
        this.shopName = shopName;
        this.imagesPath = imagesPath;
        this.productName = productName;
        this.orderName = orderName;
        this.realAmt = realAmt;
        this.orderStatus = orderStatus;
        this.payType = payType;
        this.payStatus = payStatus;
        this.createTime = createTime;
        this.score = score;
        this.scoreAmt = scoreAmt;
        this.nickname = nickname;
        this.memberName = memberName;
        this.leaveMsg = leaveMsg;
    }

    public String getOrderStatusStr() {
        return orderStatusStr;
    }

    public void setOrderStatusStr(String orderStatusStr) {
        this.orderStatusStr = orderStatusStr;
    }

    public String getPayTypeStr() {
        return payTypeStr;
    }

    public void setPayTypeStr(String payTypeStr) {
        this.payTypeStr = payTypeStr;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getRealAmt() {
        return realAmt;
    }

    public void setRealAmt(String realAmt) {
        this.realAmt = realAmt;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScoreAmt() {
        return scoreAmt;
    }

    public void setScoreAmt(String scoreAmt) {
        this.scoreAmt = scoreAmt;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getLeaveMsg() {
        return leaveMsg;
    }

    public void setLeaveMsg(String leaveMsg) {
        this.leaveMsg = leaveMsg;
    }

    public String getDueAmt() {
        return dueAmt == null ? "" : dueAmt;
    }

    public void setDueAmt(String dueAmt) {
        this.dueAmt = dueAmt;
    }
}
