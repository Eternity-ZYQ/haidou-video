package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/8 4:32 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   :  专区摸块
 * </pre>
 */
public class SectionTaskBean {


    // 商家任务编号
    private String merchantTaskNo;
    // 商家任务名称
    private String merchantTaskName;
    // 商家任务标语
    private String merchantTaskTitle;
    // 商家任务类型  rec：推荐任务     merchants：商家任务
    private String merchantTaskType;
    // 商家任务代码
    private String merchantTaskCode;
    private String merchantTaskImage;
    // 商家任务介绍
    private String merchantTaskIntroduct;
    // 所属区块编号
    private String blockNo;
    // 买单数量
    private int buyNum;
    // 卖单数量
    private int sellNum;
    // 买单活跃度
    private int buyLiveness;
    // 卖单活跃度
    private int sellLiveness;
    // 供应价格万分比
    private int supplyPercent;
    // 服务抽成万分比
    private int servicePercent;
    // 销售价格
    private int sellPrice;
    // 会员邀请奖金
    private int memberBonus;
    // 合伙人奖金
    private int partnerBonus;
    // 代金券
    private int couponAmt;
    // 保底盈利
    private int bottomAmt;
    // 推荐购买活跃度
    private int recActive;
    // 状态
    private String status;
    // 备注
    private String remark;
    // 任务编号   任务为空时，代表此任务未激活
    private String memberTaskNo;
    // 任务级别
    private String mTaskGrade;
    // 买卖开关 0 买 1卖
    private String mBuySellSet;
    // 用户买单数
    private int mBuyNum;
    // 用户卖单数
    private int mSellNum;
    // 果子数
    private String fruitCount;
    // 消耗种子数量
    private int seedNum;
    // 子任务标题
    private String recTitle;
    // 子任务激活状态，common：已激活   progress ：未激活
    private String mStatus;
    // >0 有单可抢
    private int distributeNum;
    // =1 可派单
    private int distributeStatus;

    public int getDistributeNum() {
        return distributeNum;
    }

    public void setDistributeNum(int distributeNum) {
        this.distributeNum = distributeNum;
    }

    public int getDistributeStatus() {
        return distributeStatus;
    }

    public void setDistributeStatus(int distributeStatus) {
        this.distributeStatus = distributeStatus;
    }

    public String getMStatus() {
        return mStatus == null ? "" : mStatus;
    }

    public void setMStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getRecTitle() {
        return recTitle == null ? "" : recTitle;
    }

    public void setRecTitle(String recTitle) {
        this.recTitle = recTitle;
    }

    public int getSeedNum() {
        return seedNum;
    }

    public void setSeedNum(int seedNum) {
        this.seedNum = seedNum;
    }

    public int getMBuyNum() {
        return mBuyNum;
    }

    public int getMSellNum() {
        return mSellNum;
    }

    public String getMerchantTaskNo() {
        return merchantTaskNo == null ? "" : merchantTaskNo;
    }

    public void setMerchantTaskNo(String merchantTaskNo) {
        this.merchantTaskNo = merchantTaskNo;
    }

    public String getMerchantTaskName() {
        return merchantTaskName == null ? "" : merchantTaskName;
    }

    public void setMerchantTaskName(String merchantTaskName) {
        this.merchantTaskName = merchantTaskName;
    }

    public String getMerchantTaskTitle() {
        return merchantTaskTitle == null ? "" : merchantTaskTitle;
    }

    public void setMerchantTaskTitle(String merchantTaskTitle) {
        this.merchantTaskTitle = merchantTaskTitle;
    }

    public String getMerchantTaskType() {
        return merchantTaskType == null ? "" : merchantTaskType;
    }

    public void setMerchantTaskType(String merchantTaskType) {
        this.merchantTaskType = merchantTaskType;
    }

    public String getMerchantTaskCode() {
        return merchantTaskCode == null ? "" : merchantTaskCode;
    }

    public void setMerchantTaskCode(String merchantTaskCode) {
        this.merchantTaskCode = merchantTaskCode;
    }

    public String getMerchantTaskImage() {
        return merchantTaskImage == null ? "" : merchantTaskImage;
    }

    public void setMerchantTaskImage(String merchantTaskImage) {
        this.merchantTaskImage = merchantTaskImage;
    }

    public String getMerchantTaskIntroduct() {
        return merchantTaskIntroduct == null ? "" : merchantTaskIntroduct;
    }

    public void setMerchantTaskIntroduct(String merchantTaskIntroduct) {
        this.merchantTaskIntroduct = merchantTaskIntroduct;
    }

    public String getBlockNo() {
        return blockNo == null ? "" : blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getSellNum() {
        return sellNum;
    }


    public String getFruitCount() {
        return fruitCount == null ? "" : fruitCount;
    }

    public void setFruitCount(String fruitCount) {
        this.fruitCount = fruitCount;
    }

    public void setSellNum(int sellNum) {
        this.sellNum = sellNum;
    }

    public int getBuyLiveness() {
        return buyLiveness;
    }

    public void setBuyLiveness(int buyLiveness) {
        this.buyLiveness = buyLiveness;
    }

    public int getSellLiveness() {
        return sellLiveness;
    }

    public void setSellLiveness(int sellLiveness) {
        this.sellLiveness = sellLiveness;
    }

    public int getSupplyPercent() {
        return supplyPercent;
    }

    public void setSupplyPercent(int supplyPercent) {
        this.supplyPercent = supplyPercent;
    }

    public int getServicePercent() {
        return servicePercent;
    }

    public void setServicePercent(int servicePercent) {
        this.servicePercent = servicePercent;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getMemberBonus() {
        return memberBonus;
    }

    public void setMemberBonus(int memberBonus) {
        this.memberBonus = memberBonus;
    }

    public int getPartnerBonus() {
        return partnerBonus;
    }

    public void setPartnerBonus(int partnerBonus) {
        this.partnerBonus = partnerBonus;
    }

    public int getCouponAmt() {
        return couponAmt;
    }

    public void setCouponAmt(int couponAmt) {
        this.couponAmt = couponAmt;
    }

    public int getBottomAmt() {
        return bottomAmt;
    }

    public void setBottomAmt(int bottomAmt) {
        this.bottomAmt = bottomAmt;
    }

    public int getRecActive() {
        return recActive;
    }

    public void setRecActive(int recActive) {
        this.recActive = recActive;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMemberTaskNo() {
        return memberTaskNo == null ? "" : memberTaskNo;
    }

    public void setMemberTaskNo(String memberTaskNo) {
        this.memberTaskNo = memberTaskNo;
    }

    public String getTaskGrade() {
        return mTaskGrade == null ? "" : mTaskGrade;
    }

    public void setTaskGrade(String taskGrade) {
        mTaskGrade = taskGrade;
    }

    public String getBuySellSet() {
        return mBuySellSet == null ? "" : mBuySellSet;
    }

    public void setBuySellSet(String buySellSet) {
        mBuySellSet = buySellSet;
    }
}
