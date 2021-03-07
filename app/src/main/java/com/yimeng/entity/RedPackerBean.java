package com.yimeng.entity;

import com.yimeng.utils.UnitUtil;

import java.io.Serializable;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/27 6:25 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 红包实体
 * </pre>
 */
public class RedPackerBean implements Serializable {


    private static final long serialVersionUID = 1076978774343814885L;
    /**
     * id : 229
     * modeProjectNo : MP201908271409352463509
     * memberNo : ANDROID201906152155210923271
     * projectType : yuanchuang
     * externalLinks : null
     * projectLogo : /fileupload/shopImage/20190823/1566546181311647905042(433x157).jpg
     * projectTitle : 12312
     * projectContent : 123121231少时诵诗书所多撒奥所多打萨达十大所多撒大所大所大
     * greetingCardModeNo : null
     * hadRed : 0
     * shareNum : null
     * sharePrice : null
     * nextSharePrice : null
     * sharePaidNum : null
     * nowHadPaidNum : null
     * nextHadPaidNum : null
     * shareTotalPrice : null
     * shareGetPrice : null
     * shareProvince : null
     * shareCity : null
     * scanNum : 4
     * status : common
     * createTime : 1566886175000
     * updateTime : 1566887038000
     */

    private String modeProjectNo;
    private String memberNo;
    private String projectType;
    private String externalLinks;// default:新老用户，new仅新用户
    private String projectLogo;
    private String projectTitle;
    private String projectContent;
    private Object greetingCardModeNo;
    private int hadRed;// 0 无 1有红包
    private int shareNum;//shareNum
    private String sharePrice;// 新用户红包单价
    private String nextSharePrice;// 老用户红包单价
    private int sharePaidNum; // 新用户红包个数
    private int nowHadPaidNum;//新用户剩余红包个数
    private int oldUserNum; // 老用户红包个数
    private int nextHadPaidNum;//老用户剩余红包个数
    private String payType;
    private String shareTotalPrice;//总金额
    private String shareGetPrice;// 新用户剩余总价
    private String oldGetPrice;// 老用户剩余总价
    private Object shareProvince;
    private Object shareCity;
    private int scanNum; // 阅读数量
    private String status;
    private String memberName;
    private String nickName;
    private String mobileNo;
    private String headPath;
    private long createTime;
    private long updateTime;

    /**
     * @return 中文支付方式
     */
    public String getPayTypeEn(){
        if(getPayType().equals("weixin")) {
            return "微信支付";
        }else if("zhifubao".equals(this.payType)) {
            return "支付宝支付";
        }else{
            return "余额支付";
        }
    }

    /**
     * @return 新用户总价
     */
    public double getNewSum(){
        return getSharePaidNum() * UnitUtil.getInt(getSharePrice())/100.0;
    }

    /**
     * @return 老用户总价
     */
    public double getOldSum(){
        return getOldUserNum() * UnitUtil.getInt(getNextSharePrice())/100.0;
    }

    public String getMemberName() {
        return memberName == null ? "" : memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getNickName() {
        return nickName == null ? "" : nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobileNo() {
        return mobileNo == null ? "" : mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getHeadPath() {
        return headPath == null ? "" : headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getModeProjectNo() {
        return modeProjectNo == null ? "" : modeProjectNo;
    }

    public void setModeProjectNo(String modeProjectNo) {
        this.modeProjectNo = modeProjectNo;
    }

    public String getMemberNo() {
        return memberNo == null ? "" : memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getProjectType() {
        return projectType == null ? "" : projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getExternalLinks() {
        return externalLinks;
    }

    public void setExternalLinks(String externalLinks) {
        this.externalLinks = externalLinks;
    }

    public String getProjectLogo() {
        return projectLogo == null ? "" : projectLogo;
    }

    public void setProjectLogo(String projectLogo) {
        this.projectLogo = projectLogo;
    }

    public String getProjectTitle() {
        return projectTitle == null ? "" : projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectContent() {
        return projectContent == null ? "" : projectContent;
    }

    public void setProjectContent(String projectContent) {
        this.projectContent = projectContent;
    }

    public Object getGreetingCardModeNo() {
        return greetingCardModeNo;
    }

    public void setGreetingCardModeNo(Object greetingCardModeNo) {
        this.greetingCardModeNo = greetingCardModeNo;
    }

    public int getHadRed() {
        return hadRed;
    }

    public void setHadRed(int hadRed) {
        this.hadRed = hadRed;
    }

    public int getShareNum() {
        return shareNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

    public String getSharePrice() {
        return sharePrice == null ? "" : sharePrice;
    }

    public void setSharePrice(String sharePrice) {
        this.sharePrice = sharePrice;
    }

    public String getNextSharePrice() {
        return nextSharePrice == null ? "" : nextSharePrice;
    }

    public void setNextSharePrice(String nextSharePrice) {
        this.nextSharePrice = nextSharePrice;
    }

    public int getSharePaidNum() {
        return sharePaidNum;
    }

    public void setSharePaidNum(int sharePaidNum) {
        this.sharePaidNum = sharePaidNum;
    }

    public int getOldUserNum() {
        return oldUserNum;
    }

    public void setOldUserNum(int oldUserNum) {
        this.oldUserNum = oldUserNum;
    }

    public int getNowHadPaidNum() {
        return nowHadPaidNum;
    }

    public void setNowHadPaidNum(int nowHadPaidNum) {
        this.nowHadPaidNum = nowHadPaidNum;
    }

    public int getNextHadPaidNum() {
        return nextHadPaidNum;
    }

    public void setNextHadPaidNum(int nextHadPaidNum) {
        this.nextHadPaidNum = nextHadPaidNum;
    }

    public String getPayType() {
        return payType == null ? "" : payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getShareTotalPrice() {
        return shareTotalPrice == null ? "" : shareTotalPrice;
    }

    public void setShareTotalPrice(String shareTotalPrice) {
        this.shareTotalPrice = shareTotalPrice;
    }

    public String getShareGetPrice() {
        return shareGetPrice == null ? "" : shareGetPrice;
    }

    public void setShareGetPrice(String shareGetPrice) {
        this.shareGetPrice = shareGetPrice;
    }

    public String getOldGetPrice() {
        return oldGetPrice == null ? "" : oldGetPrice;
    }

    public void setOldGetPrice(String oldGetPrice) {
        this.oldGetPrice = oldGetPrice;
    }

    public Object getShareProvince() {
        return shareProvince;
    }

    public void setShareProvince(Object shareProvince) {
        this.shareProvince = shareProvince;
    }

    public Object getShareCity() {
        return shareCity;
    }

    public void setShareCity(Object shareCity) {
        this.shareCity = shareCity;
    }

    public int getScanNum() {
        return scanNum;
    }

    public void setScanNum(int scanNum) {
        this.scanNum = scanNum;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
