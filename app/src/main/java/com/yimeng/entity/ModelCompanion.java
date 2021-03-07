package com.yimeng.entity;

import java.io.Serializable;

/**
 * 我的伙伴
 *
 * @author xp
 * @describe 我的伙伴.
 * @date 2018/7/31.
 */

public class ModelCompanion implements Serializable {

    private String headPath;// 头像图片路径
    private String nickname;// 昵称
    private String mobileNo;// 手机号
    private long createTime;// 注册时间
    private String memberTypeName; // 用户角色
    private String gradeName;// 等级
    private String shopNames;//店铺名称
    private String telephone;//店铺
    private String memberNo;//用户编号
    private String tuijianMemberName; //买卖任务状态 common（卖） ，freeze（买）
    private String certType;//等于agent为合伙人

    public ModelCompanion(String headPath, String nickname, String mobileNo, long createTime) {
        this.headPath = headPath;
        this.nickname = nickname;
        this.mobileNo = mobileNo;
        this.createTime = createTime;
    }
    public ModelCompanion(String headPath, String nickname, String mobileNo, long createTime, String memberTypeName) {
        this(headPath, nickname,mobileNo,createTime);
        this.memberTypeName = memberTypeName;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getMemberTypeName() {
        return memberTypeName == null ? "" : memberTypeName;
    }

    public void setMemberTypeName(String memberTypeName) {
        this.memberTypeName = memberTypeName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getShopNames() {
        return shopNames;
    }

    public void setShopNames(String shopNames) {
        this.shopNames = shopNames;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMemberNo() {
        return memberNo == null ? "" : memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getTuijianMemberName() {
        return tuijianMemberName == null ? "" : tuijianMemberName;
    }

    public void setTuijianMemberName(String tuijianMemberName) {
        this.tuijianMemberName = tuijianMemberName;
    }

    public String getCertType() {
        return certType == null ? "" : certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }
}
