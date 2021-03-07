package com.yimeng.entity;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * 店铺订单
 *
 * @author lijb
 */

public class ShopOrderModel implements Serializable {

    private String orderNo;//编号
    private String realPrice;
    private String dueAmt;//实付金额
    private String orderStatus;//订单状态 new(新订单)，complete（已完成）
    private String payStatus;// 支付状态
    private String score;//返还麻豆
    private String insuranceOrder_orderNo;//单号 延保订单
    private String insuranceOrder_realPrice;//价格 延保订单
    private String insuranceOrder_orderStatus;//订单状态 延保订单
    private String member_memberNo;//用户信息 用户编号
    private String member_memberName;//用户信息 姓名
    private String member_nickname;//用户信息 昵称
    private String member_mobileNo;//手机号用户信息
    private String member_headPath;//头像 //用户信息
    private int isDelivery; // 是否送货上门
    private String address;// 收货地址
    private String receiver;// 收货人
    private String receiverMobile;// 收货手机号
    private String sendInfo;// 取件信息
    private long createTime;// 下单时间
    private ModelGrade modelGrade;//评论信息
    private Long sendTime;// 送货时间
    //  0普通商品, 1平台促销, 3自促商品
    private String isPlatno;
    // 快递类型
    private String logisticsType;
    // 快递名称
    private String logisticsName;
    // 快递编号
    private String logisticsNo;
    // 快递费
    private String logisticsFee;
    // 支付方式
    private String payTypeStr;
    private String leaveMsg;// 买家留言
    private String couponAmt; // 代金券

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    private LinkedList<ModelOrderGoods> goodsModels;//产品列表

    public ShopOrderModel(String orderNo, String realPrice, String orderStatus, String payStatus, String score, String insuranceOrder_orderNo, String insuranceOrder_realPrice, String insuranceOrder_orderStatus, String member_memberNo, String member_memberName, String member_nickname, String member_mobileNo, String member_headPath, ModelGrade modelGrade, LinkedList<ModelOrderGoods> goodsModels) {
        this.orderNo = orderNo;
        this.realPrice = realPrice;
        this.orderStatus = orderStatus;
        this.payStatus = payStatus;
        this.score = score;
        this.insuranceOrder_orderNo = insuranceOrder_orderNo;
        this.insuranceOrder_realPrice = insuranceOrder_realPrice;
        this.insuranceOrder_orderStatus = insuranceOrder_orderStatus;
        this.member_memberNo = member_memberNo;
        this.member_memberName = member_memberName;
        this.member_nickname = member_nickname;
        this.member_mobileNo = member_mobileNo;
        this.member_headPath = member_headPath;
        this.modelGrade = modelGrade;
        this.goodsModels = goodsModels;
    }

    public String getDueAmt() {
        return dueAmt == null ? "" : dueAmt;
    }

    public void setDueAmt(String dueAmt) {
        this.dueAmt = dueAmt;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getInsuranceOrder_orderNo() {
        return insuranceOrder_orderNo;
    }

    public void setInsuranceOrder_orderNo(String insuranceOrder_orderNo) {
        this.insuranceOrder_orderNo = insuranceOrder_orderNo;
    }

    public String getInsuranceOrder_realPrice() {
        return insuranceOrder_realPrice;
    }

    public void setInsuranceOrder_realPrice(String insuranceOrder_realPrice) {
        this.insuranceOrder_realPrice = insuranceOrder_realPrice;
    }

    public String getInsuranceOrder_orderStatus() {
        return insuranceOrder_orderStatus;
    }

    public void setInsuranceOrder_orderStatus(String insuranceOrder_orderStatus) {
        this.insuranceOrder_orderStatus = insuranceOrder_orderStatus;
    }

    public String getMember_memberNo() {
        return member_memberNo;
    }

    public void setMember_memberNo(String member_memberNo) {
        this.member_memberNo = member_memberNo;
    }

    public String getMember_memberName() {
        return member_memberName;
    }

    public void setMember_memberName(String member_memberName) {
        this.member_memberName = member_memberName;
    }

    public String getMember_nickname() {
        return member_nickname;
    }

    public void setMember_nickname(String member_nickname) {
        this.member_nickname = member_nickname;
    }

    public String getMember_mobileNo() {
        return member_mobileNo;
    }

    public void setMember_mobileNo(String member_mobileNo) {
        this.member_mobileNo = member_mobileNo;
    }

    public String getMember_headPath() {
        return member_headPath;
    }

    public void setMember_headPath(String member_headPath) {
        this.member_headPath = member_headPath;
    }

    public ModelGrade getModelGrade() {
        return modelGrade;
    }

    public void setModelGrade(ModelGrade modelGrade) {
        this.modelGrade = modelGrade;
    }

    public LinkedList<ModelOrderGoods> getGoodsModels() {
        return goodsModels;
    }

    public void setGoodsModels(LinkedList<ModelOrderGoods> goodsModels) {
        this.goodsModels = goodsModels;
    }

    public int getIsDelivery() {
        return isDelivery;
    }

    public void setIsDelivery(int isDelivery) {
        this.isDelivery = isDelivery;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReceiver() {
        return receiver == null ? "" : receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverMobile() {
        return receiverMobile == null ? "" : receiverMobile;
    }

    public String getSendInfo() {
        return sendInfo == null ? "" : sendInfo;
    }

    public void setSendInfo(String sendInfo) {
        this.sendInfo = sendInfo;
    }

    public void setReceiverMobile(String receiverMobile) {

        this.receiverMobile = receiverMobile;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getIsPlatno() {
        return isPlatno == null ? "" : isPlatno;
    }

    public void setIsPlatno(String isPlatno) {
        this.isPlatno = isPlatno;
    }

    public String getLogisticsType() {
        return logisticsType == null ? "" : logisticsType;
    }

    public void setLogisticsType(String logisticsType) {
        this.logisticsType = logisticsType;
    }

    public String getLogisticsNo() {
        return logisticsNo == null ? "" : logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    public String getLogisticsName() {
        return logisticsName == null ? "" : logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public String getPayTypeStr() {
        return payTypeStr == null ? "" : payTypeStr;
    }

    public void setPayTypeStr(String payTypeStr) {
        this.payTypeStr = payTypeStr;
    }

    public String getLeaveMsg() {
        return leaveMsg == null ? "" : leaveMsg;
    }

    public void setLeaveMsg(String leaveMsg) {
        this.leaveMsg = leaveMsg;
    }

    public String getLogisticsFee() {
        return logisticsFee == null ? "" : logisticsFee;
    }

    public void setLogisticsFee(String logisticsFee) {
        this.logisticsFee = logisticsFee;
    }
    public String getCouponAmt() {
        return couponAmt == null ? "" : couponAmt;
    }

    public void setCouponAmt(String couponAmt) {
        this.couponAmt = couponAmt;
    }

}
