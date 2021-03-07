package com.yimeng.entity;

import android.text.TextUtils;

import com.yimeng.utils.UnitUtil;

import java.io.Serializable;

/**
 * 用户基本信息
 */
public class Member implements Serializable {

    /**
     * 用户编号
     */
    private String memberNo;

    /**
     * 用户手机号
     */
    private String mobileNo;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户姓名
     */
    private String memberName;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 用户头像
     */
    private String headPath;

    /**
     * token
     */
    private String token;

    /**
     * 用户唯一识别码
     */
    private String bindNo;

    /**
     * 用户角色
     */
    private String memberType;

    /**
     * 认证情况
     */
    private String nameAuthFlag;
    /**
     * 是否开店
     * open ：开店
     */
    private String job;
    /**
     * 是否为代理
     * agent ：是代理
     */
    private String certType;

    /**
     * 店铺号
     */
    private String telePhone;
    /**
     * 用户等级
     */
    private int memberGrade;
    /**
     * 用户等级名称
     */
    private String memberGradeAlias;
    /**
     * 用户等级名称
     */
    private String memberGradeName;

    /**
     * 微信授权openid
     */
    private String openid;
    /**
     * 微信昵称
     */
    private String nameMM;
    /**
     * 支付宝unionid
     */
    private String unionid;
    /**
     * 支付宝昵称
     */
    private String nameMMphone;

    /**
     * 活跃度
     */
    private long extracts;
    /**
     * vip标识
     */
    private String vipType;
    /**
     * vip到期时间
     */
    private long vipEndTime;
    /**
     * 是否设置支付密码
     */
    private String payCodeStatus;
    /**
     * 是否长传过通讯录
     * yes  上传过
     * no   未上传
     */
    private String memberBookStatus;
    /**
     * 最后一次上传时间
     */
    private long memberBookLastUploadTime;
    /**
     * 种子数量
     */
    private int seedCount;
    /**
     * 生长激素
     */
    private int hormone;
    /**
     * 日生长激素
     */
    private int dayHormone;

    /**
     * 活跃果数量
     */
    private int fruitCount;
    /**
     * 是否激活  1 激活  0 未激活
     */
    private int isOldUsr;

    /**
     * 看5次广告 如果和今天时间相同代表完成
     */
    private long contractTime;

    /**
     * 看5分钟视频 如果和今天时间相同代表完成
     */
    private long amtTime;

    /**
     * 邀请码
     */
    private String inviteCode;

    public int getFruitCount() {
        return fruitCount;
    }

    public void setFruitCount(int fruitCount) {
        this.fruitCount = fruitCount;
    }

    public int getDayHormone() {
        return dayHormone;
    }

    public void setDayHormone(int dayHormone) {
        this.dayHormone = dayHormone;
    }

    public int getHormone() {
        return hormone;
    }

    public void setHormone(int hormone) {
        this.hormone = hormone;
    }

    public int getSeedCount() {
        return seedCount;
    }

    public void setSeedCount(int seedCount) {
        this.seedCount = seedCount;
    }

    public String getMemberNo() {
        return memberNo == null ? "" : memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
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

    public String getMemberName() {
        return memberName == null ? "" : memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getSex() {
        return sex == null ? "" : sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadPath() {
        return headPath == null ? "" : headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBindNo() {
        return bindNo == null ? "" : bindNo;
    }

    public void setBindNo(String bindNo) {
        this.bindNo = bindNo;
    }

    public String getMemberType() {
        return memberType == null ? "" : memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getNameAuthFlag() {
        return nameAuthFlag == null ? "" : nameAuthFlag;
    }

    public void setNameAuthFlag(String nameAuthFlag) {
        this.nameAuthFlag = nameAuthFlag;
    }

    public String getJob() {
        return job == null ? "" : job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getTelePhone() {
        return telePhone == null ? "" : telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public int getMemberGrade() {
        return memberGrade;
    }

    public void setMemberGrade(int memberGrade) {
        this.memberGrade = memberGrade;
    }

    public String getMemberGradeAlias() {
        return memberGradeAlias == null ? "" : memberGradeAlias;
    }

    public void setMemberGradeAlias(String memberGradeAlias) {
        this.memberGradeAlias = memberGradeAlias;
    }

    public String getOpenid() {
        return openid == null ? "" : openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNameMM() {
        return nameMM == null ? "" : nameMM;
    }

    public void setNameMM(String nameMM) {
        this.nameMM = nameMM;
    }

    public String getUnionid() {
        return unionid == null ? "" : unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getNameMMphone() {
        return nameMMphone == null ? "" : nameMMphone;
    }

    public void setNameMMphone(String nameMMphone) {
        this.nameMMphone = nameMMphone;
    }

    public String getCertType() {
        return certType == null ? "" : certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public long getExtracts() {
        return extracts;
    }

    public void setExtracts(long extracts) {
        this.extracts = extracts;
    }

    public String getVipType() {
        return vipType == null ? "" : vipType;
    }

    public void setVipType(String vipType) {
        this.vipType = vipType;
    }

    public long getVipEndTime() {
        return vipEndTime;
    }

    public void setVipEndTime(long vipEndTime) {
        this.vipEndTime = vipEndTime;
    }

    public String getPayCodeStatus() {
        return payCodeStatus == null ? "" : payCodeStatus;
    }

    public void setPayCodeStatus(String payCodeStatus) {
        this.payCodeStatus = payCodeStatus;
    }

    public String getMemberBookStatus() {
        return memberBookStatus == null ? "" : memberBookStatus;
    }

    public void setMemberBookStatus(String memberBookStatus) {
        this.memberBookStatus = memberBookStatus;
    }

    public long getMemberBookLastUploadTime() {
        return memberBookLastUploadTime;
    }

    public void setMemberBookLastUploadTime(long memberBookLastUploadTime) {
        this.memberBookLastUploadTime = memberBookLastUploadTime;
    }

    public int getIsOldUsr() {
        return isOldUsr;
    }

    public void setIsOldUsr(int isOldUsr) {
        this.isOldUsr = isOldUsr;
    }

    public long getContractTime() {
        return contractTime;
    }

    public void setContractTime(long contractTime) {
        this.contractTime = contractTime;
    }

    public long getAmtTime() {
        return amtTime;
    }

    public void setAmtTime(long amtTime) {
        this.amtTime = amtTime;
    }

    public String getMemberGradeName() {
        return memberGradeName == null ? "" : memberGradeName;
    }

    public void setMemberGradeName(String memberGradeName) {
        this.memberGradeName = memberGradeName;
    }

    public String getInviteCode() {
        return inviteCode == null ? "" : inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    /*---------------------------------------------------------------------------------------------*/

    /**
     * @return 当前活跃度
     */
    public String getExtractStr() {
        double d;
        if (extracts < 9999) {
            return String.valueOf(extracts);
        } else if (extracts < 99999999) {
            d = extracts / 10000.0;
            return UnitUtil.get2DecimalPointData(d) + "万";
        } else {
            d = extracts / 100000000.0;
            return UnitUtil.get2DecimalPointData(d) + "亿";
        }
    }

    /**
     * @return 是否为vip
     */
    public boolean isVIP() {
        return !TextUtils.isEmpty(getVipType()) && getVipEndTime() > System.currentTimeMillis();
    }


    /**
     * @return 是否为合伙人
     */
    public boolean isAgent() {
        return getCertType().equals("agent");
    }


    /**
     * @return 是否设置支付密码
     */
    public boolean isSetPayPwd() {
        if(payCodeStatus == null) {
            return false;
        }
        return payCodeStatus.equals("yes");
    }

    /**
     * @return 是否需要上传通讯录
     */
    public boolean isCanUploadContacts() {
        if (getMemberBookStatus().equals("yes")) {
            long currentTimeMillis = System.currentTimeMillis();
            // 上传超过一年，需要重新上传
            // 上传未超过一年
            return currentTimeMillis - getMemberBookLastUploadTime() > 365 * 24 * 60 * 60 * 1000;
        } else {
            // 未上传过
            return true;
        }
    }

}
