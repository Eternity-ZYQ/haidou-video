package com.yimeng.entity;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * 我的订单-店铺订单
 */

public class ModelShopOrderList implements Serializable {

    private String orderName;// 订单名称
    private String orderNo;//订单编号
    private String shopName;//店铺名称
    private String shopNo;//店铺
    private String shopLogoPath;//店铺logo
    private String refund;//返还金额
    private String score;//返还麻豆
    private String orderStatus;//订单状态
    private String remark;// 全保订单编号
    private ModelGrade modelGrade;//评论信息
    private String sendInfo;// 快递物流信息
    private long createTime;// 下单时间
    private String address;// 收件地址
    private String receiver;// 收件人
    private String receiverMobile;// 收件手机
    private String logisticsNo;// 物流编号
    private String logisticsName;//物流类型中文
    private String logisticsType;//物流类型英文
    private String logisticsFee;// 运费
    private String couponAmt; // 代金券
    private String realAmt;
    private String payType;//支付方式
    private String payTypeStr;// 支付方式
    private String leaveMsg;// 买家留言
    private String invoiceName;//不为空，表示已催单
    private String isPlatno; //为1 厂促商品
    private String balance;//余额支付时的实付金额

    private LinkedList<ModelShopOrderListItem> modelShopOrderListItemsList;

    public ModelShopOrderList(String orderName, String orderNo, String shopName, String shopNo,
                              String shopLogoPath, String refund, String orderStatus, ModelGrade modelGrade,
                              LinkedList<ModelShopOrderListItem> modelShopOrderListItemsList) {
        this.orderName = orderName;
        this.orderNo = orderNo;
        this.shopName = shopName;
        this.shopNo = shopNo;
        this.shopLogoPath = shopLogoPath;
        this.refund = refund;
        this.orderStatus = orderStatus;
        this.modelGrade = modelGrade;
        this.modelShopOrderListItemsList = modelShopOrderListItemsList;
    }

    /**
     * 获取实付金额
     * @return
     */
    public String getRealMoney(){
        if(payType.equals("yu_e")) {// 余额支付时取balance字段
            return balance;
        }else{
            return realAmt;
        }
    }

    public String getPayType() {
        return payType == null ? "" : payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public ModelGrade getModelGrade() {
        return modelGrade;
    }

    public void setModelGrade(ModelGrade modelGrade) {
        this.modelGrade = modelGrade;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getShopLogoPath() {
        return shopLogoPath;
    }

    public void setShopLogoPath(String shopLogoPath) {
        this.shopLogoPath = shopLogoPath;
    }

    public LinkedList<ModelShopOrderListItem> getModelShopOrderListItemsList() {
        return modelShopOrderListItemsList;
    }

    public void setModelShopOrderListItemsList(LinkedList<ModelShopOrderListItem> modelShopOrderListItemsList) {
        this.modelShopOrderListItemsList = modelShopOrderListItemsList;
    }

    public String getSendInfo() {
        return sendInfo == null ? "" : sendInfo;
    }

    public void setSendInfo(String sendInfo) {
        this.sendInfo = sendInfo;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
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

    public String getLogisticsType() {
        return logisticsType == null ? "" : logisticsType;
    }

    public void setLogisticsType(String logisticsType) {
        this.logisticsType = logisticsType;
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

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getCouponAmt() {
        return couponAmt == null ? "" : couponAmt;
    }

    public void setCouponAmt(String couponAmt) {
        this.couponAmt = couponAmt;
    }

    public String getRealAmt() {
        return realAmt == null ? "" : realAmt;
    }

    public void setRealAmt(String realAmt) {
        this.realAmt = realAmt;
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

    public String getInvoiceName() {
        return invoiceName == null ? "" : invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public String getIsPlatno() {
        return isPlatno == null ? "" : isPlatno;
    }

    public void setIsPlatno(String isPlatno) {
        this.isPlatno = isPlatno;
    }

    public String getBalance() {
        return balance == null ? "" : balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
