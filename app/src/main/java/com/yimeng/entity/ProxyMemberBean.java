package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/4/16 5:18 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 代理用户
 * </pre>
 */
public class ProxyMemberBean {
    private String memberNo; // 会员编号
    private String memberName; // 会员姓名
    private int memberGrade; // 会员等级
    private String gradeName;// 等级名称（黄牛）
    private String memberType; //会员类型
    private String mobileNo; // 手机号
    private String nickname; // 昵称
    private String loginName; // 登录名
    private String headPath; // 头像地址
    private String joinedRemark; //代理结算标识 settlement(已结算)
    private long contractTime; //结算时间
    private String shopName; // 店铺名称
    private String job;//是否开店

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

    public int getMemberGrade() {
        return memberGrade;
    }

    public void setMemberGrade(int memberGrade) {
        this.memberGrade = memberGrade;
    }

    public String getMemberType() {
        return memberType == null ? "" : memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getMobileNo() {
        return mobileNo == null ? "" : mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLoginName() {
        return loginName == null ? "" : loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getHeadPath() {
        return headPath == null ? "" : headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getJoinedRemark() {
        return joinedRemark == null ? "" : joinedRemark;
    }

    public void setJoinedRemark(String joinedRemark) {
        this.joinedRemark = joinedRemark;
    }

    public long getContractTime() {
        return contractTime;
    }

    public void setContractTime(long contractTime) {
        this.contractTime = contractTime;
    }

    public String getGradeName() {
        return gradeName == null ? "" : gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getShopName() {
        return shopName == null ? "" : shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getJob() {
        return job == null ? "" : job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
