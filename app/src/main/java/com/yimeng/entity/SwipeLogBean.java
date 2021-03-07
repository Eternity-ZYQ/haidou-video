package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/30 0030 下午 07;//54.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class SwipeLogBean {
    private String id;//id
    private String orderNo;//订单编号
    private String orderName;//订单名称, (结算名称,结算卡号)
    private String questNo;//交易请求号
    private String ysoderNo;//银盛流水号
    private String merchantCode;//商户号
    private String statusTwo;//刷卡状态
    private String orderStatus;//订单状态
    private String orderAmount;//分润金额/订单金额
    private String orderToSplit_no;//原支付订单号
    private String orderType;//订单类型
    private String orderCategory;//订单分类
    private String originalPrice;//订单原价, 费率
    private String realPrice;//订单实际价格
    private String dueAmt;//应付交易金额
    private String realAmt;//实付交易金额
    private String payType;//支付类型
    private String payStatus;//支付状态
    private String memberNo;//会员编号
    private String memberName;//会员名称
    private String nickname;//会员昵称
    private String memberHeadPath;//会员头像
    private String withdrawal;//提现金额
    private String cardIssuer;//发卡机构
    private String settlementCard;//结算卡号
    private String transactionTime;//交易时间
    private String settlementMethod;//结算方式
    private String settlementStatus;//结算状态
    private String arrivalAccount;//到账金额
    private String remindAmt;//平台获取金额
    private String remark;//备注
    private String deleteStatus;//删除状态
    private String processPercentage;//费率, 万分比
    private String process;//手续费
    private String serviceCharge;//交易手续费

    private long createTime;//创建时间
    private long updateTime;//更新时间

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getQuestNo() {
        return questNo == null ? "" : questNo;
    }

    public void setQuestNo(String questNo) {
        this.questNo = questNo;
    }

    public String getYsoderNo() {
        return ysoderNo == null ? "" : ysoderNo;
    }

    public void setYsoderNo(String ysoderNo) {
        this.ysoderNo = ysoderNo;
    }

    public String getMerchantCode() {
        return merchantCode == null ? "" : merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getStatusTwo() {
        return statusTwo == null ? "" : statusTwo;
    }

    public void setStatusTwo(String statusTwo) {
        this.statusTwo = statusTwo;
    }

    public String getOrderStatus() {
        return orderStatus == null ? "" : orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderAmount() {
        return orderAmount == null ? "" : orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderToSplit_no() {
        return orderToSplit_no == null ? "" : orderToSplit_no;
    }

    public void setOrderToSplit_no(String orderToSplit_no) {
        this.orderToSplit_no = orderToSplit_no;
    }

    public String getOrderType() {
        return orderType == null ? "" : orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderCategory() {
        return orderCategory == null ? "" : orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    public String getOriginalPrice() {
        return originalPrice == null ? "" : originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getRealPrice() {
        return realPrice == null ? "" : realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }

    public String getDueAmt() {
        return dueAmt == null ? "" : dueAmt;
    }

    public void setDueAmt(String dueAmt) {
        this.dueAmt = dueAmt;
    }

    public String getRealAmt() {
        return realAmt == null ? "" : realAmt;
    }

    public void setRealAmt(String realAmt) {
        this.realAmt = realAmt;
    }

    public String getPayType() {
        return payType == null ? "" : payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayStatus() {
        return payStatus == null ? "" : payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getMemberNo() {
        return memberNo == null ? "" : memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberName() {
        return memberName == null ? "" : memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMemberHeadPath() {
        return memberHeadPath == null ? "" : memberHeadPath;
    }

    public void setMemberHeadPath(String memberHeadPath) {
        this.memberHeadPath = memberHeadPath;
    }

    public String getWithdrawal() {
        return withdrawal == null ? "" : withdrawal;
    }

    public void setWithdrawal(String withdrawal) {
        this.withdrawal = withdrawal;
    }

    public String getCardIssuer() {
        return cardIssuer == null ? "" : cardIssuer;
    }

    public void setCardIssuer(String cardIssuer) {
        this.cardIssuer = cardIssuer;
    }

    public String getSettlementCard() {
        return settlementCard == null ? "" : settlementCard;
    }

    public void setSettlementCard(String settlementCard) {
        this.settlementCard = settlementCard;
    }

    public String getTransactionTime() {
        return transactionTime == null ? "" : transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getSettlementMethod() {
        return settlementMethod == null ? "" : settlementMethod;
    }

    public void setSettlementMethod(String settlementMethod) {
        this.settlementMethod = settlementMethod;
    }

    public String getSettlementStatus() {
        return settlementStatus == null ? "" : settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public String getServiceCharge() {
        return serviceCharge == null ? "" : serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getArrivalAccount() {
        return arrivalAccount == null ? "" : arrivalAccount;
    }

    public void setArrivalAccount(String arrivalAccount) {
        this.arrivalAccount = arrivalAccount;
    }

    public String getRemindAmt() {
        return remindAmt == null ? "" : remindAmt;
    }

    public void setRemindAmt(String remindAmt) {
        this.remindAmt = remindAmt;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDeleteStatus() {
        return deleteStatus == null ? "" : deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getProcessPercentage() {
        return processPercentage == null ? "" : processPercentage;
    }

    public void setProcessPercentage(String processPercentage) {
        this.processPercentage = processPercentage;
    }

    public String getProcess() {
        return process == null ? "" : process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
