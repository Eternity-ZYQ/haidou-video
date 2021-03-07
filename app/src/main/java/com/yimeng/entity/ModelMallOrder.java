package com.yimeng.entity;

import com.yimeng.utils.UnitUtil;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * 商城订单分页列表
 */

public class ModelMallOrder implements Serializable {

    LinkedList<ModelMallOrderItem> modelMallOrderItemList;
    private String orderName;// 订单名称
    private String orderNo;//订单编号
    private String shopName;//店铺名称
    private String shopNo;//店铺编号
    private String payType;//支付方式
    private String realAmt;//实付金额（微信、支付宝支付）--仅针对商城订单
    private String balance;// 实付金额（余额支付）--仅针对商城订单
    private String realPrice;
    private String refund;
    private String couponAmt;//使用的麻豆
    private String logisticsFee;//总运费
    private String logisticsType;// 物流类型
    private String logisticsNo;// 物流单号
    private String receiver;// 收货人
    private String receiverMobile;// 收货人手机号
    private String address;// 收货人详细地址
    private String commentNo;// 评论编号
    private String businessNo;// 业务编号
    private String businessType;// 业务类型
    private String memberNo;// 评论人编号
    private String discriptScore;// 评分
    private String dueAmt;
    private String content;// 内容
    private String imgPath;// 评论图片
    private String leaveMsg;// 买家留言
    private String payTypeStr;// 支付类型文字
    private String getScore;// 麻豆
    private Shop shop;
    private long payTime;
    private String orderStatus;//订单状态
    /**
     * （entryGoods 退货） （entryRefund 退款）
     */
    private String couponNo;// 区分未发货的退款中和退货后的退款
    private String invoiceEmail;// 区分未发货的退款中和退货后的退款(新2019-5-31)

    public ModelMallOrder(String orderName, String orderNo, String shopName, String shopNo,
                          String payType, String realAmt, String couponAmt, String logisticsFee,
                          String logisticsType, String logisticsNo, String receiver, String receiverMobile,
                          String address, String commentNo, String businessNo, String businessType,
                          String memberNo, String discriptScore, String content, String imgPath, String orderStatus,
                          String couponNo, LinkedList<ModelMallOrderItem> modelMallOrderItemList) {
        this.orderName = orderName;
        this.orderNo = orderNo;
        this.shopName = shopName;
        this.shopNo = shopNo;
        this.payType = payType;
        this.realAmt = realAmt;
        this.couponAmt = couponAmt;
        this.logisticsFee = logisticsFee;
        this.logisticsType = logisticsType;
        this.logisticsNo = logisticsNo;
        this.receiver = receiver;
        this.receiverMobile = receiverMobile;
        this.address = address;
        this.commentNo = commentNo;
        this.businessNo = businessNo;
        this.businessType = businessType;
        this.memberNo = memberNo;
        this.discriptScore = discriptScore;
        this.content = content;
        this.imgPath = imgPath;
        this.orderStatus = orderStatus;
        this.couponNo = couponNo;
        this.modelMallOrderItemList = modelMallOrderItemList;
    }

    public String getBalance() {
        return balance == null ? "" : balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public String getRefund() {
        return refund == null ? "" : refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public String getRealPrice() {
        return realPrice == null ? "" : realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getDueAmt() {
        return dueAmt;
    }

    public void setDueAmt(String dueAmt) {
        this.dueAmt = dueAmt;
    }

    public String getGetScore() {
        return getScore;
    }

    public void setGetScore(String getScore) {
        this.getScore = getScore;
    }

    public String getPayTypeStr() {
        return payTypeStr;
    }

    public void setPayTypeStr(String payTypeStr) {
        this.payTypeStr = payTypeStr;
    }

    public String getLeaveMsg() {
        return leaveMsg;
    }

    public void setLeaveMsg(String leaveMsg) {
        this.leaveMsg = leaveMsg;
    }

    public String getLogisticsType() {
        return logisticsType;
    }

    public void setLogisticsType(String logisticsType) {
        this.logisticsType = logisticsType;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommentNo() {
        return commentNo;
    }

    public void setCommentNo(String commentNo) {
        this.commentNo = commentNo;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getDiscriptScore() {
        return discriptScore;
    }

    public void setDiscriptScore(String discriptScore) {
        this.discriptScore = discriptScore;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getRealAmt() {
        return realAmt;
    }

    public void setRealAmt(String realAmt) {
        this.realAmt = realAmt;
    }

    public String getCouponAmt() {
        return couponAmt;
    }

    public void setCouponAmt(String couponAmt) {
        this.couponAmt = couponAmt;
    }

    public String getLogisticsFee() {
        return logisticsFee;
    }

    public void setLogisticsFee(String logisticsFee) {
        this.logisticsFee = logisticsFee;
    }

    public String getOrderStatus() {
        return orderStatus == null ? "" : orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCouponNo() {
        return couponNo == null ? "" : couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    /**
     * 实际支付金额，区分支付方式--仅针对商城订单
     */
    public String getRealMoney(){
        if(payType.equals("yu_e")) {
            // 余额支付
            return UnitUtil.getMoney(balance);
        }else{
            // 微信支付宝支付
            return UnitUtil.getMoney(realAmt);
        }
    }

    public LinkedList<ModelMallOrderItem> getModelMallOrderItemList() {
        return modelMallOrderItemList;
    }

    public void setModelMallOrderItemList(LinkedList<ModelMallOrderItem> modelMallOrderItemList) {
        this.modelMallOrderItemList = modelMallOrderItemList;
    }

    public String getInvoiceEmail() {
        return invoiceEmail == null ? "" : invoiceEmail;
    }

    public void setInvoiceEmail(String invoiceEmail) {
        this.invoiceEmail = invoiceEmail;
    }

    public static class Shop implements Serializable {

        private String mobileNo;
        private String telephone;
        private String address;

        public Shop(String mobileNo, String telephone, String address) {
            this.mobileNo = mobileNo;
            this.telephone = telephone;
            this.address = address;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
