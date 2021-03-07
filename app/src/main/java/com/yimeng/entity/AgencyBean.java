package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/30 0030 下午 08:58.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 我的代理
 * </pre>
 */
public class AgencyBean {
    private String id;//id
    private String memberNo;//会员编号
    private String memberName;//会员姓名
    private String memberGrade;//会员等级
    private String memberType;//会员类型
    private String certType;//证件类型(认证类型：identity 身份，skill 技能)
    private String certNo;//证件号码
    private String job;//店铺状态
    private String telephone;//店铺编号
    private String mobileNo;//手机号
    private String nickname;//昵称
    private String loginName;//登录名
    private String email;//邮箱
    private String lockStatus;//会员锁定状态
    private String nameAuthFlag;//实名认证标识
    private String memberSrc;//会员来源
    private String recMobile;//邀请人编号
    private String recCode;//推荐人编号
    private String headPath;//头像地址
    private String lastLoginTime;//上次登录成功时间
    private String cycleLoginFailTimes;//周期内登录失败次数
    private String isBindMobile;//是否绑定手机
    private String bindNo;//绑定手机编号
    private String psignature;//个性签名
    private String age;//年龄
    private String sex;//性别 1男性，2女性，0未知
    private String county;//国家
    private String province;//省份
    private String city;//城市
    private String area;//区域
    private String address;//具体地址
    private String blacklist;//黑名单
    private String remark;//备注
    private long createTime;//创建时间
    private long updateTime;//更新时间
    private String floor;//认证照片
    private String extracts;//代理商提成百分比

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMemberGrade() {
        return memberGrade == null ? "" : memberGrade;
    }

    public void setMemberGrade(String memberGrade) {
        this.memberGrade = memberGrade;
    }

    public String getMemberType() {
        return memberType == null ? "" : memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getCertType() {
        return certType == null ? "" : certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertNo() {
        return certNo == null ? "" : certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getJob() {
        return job == null ? "" : job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getTelephone() {
        return telephone == null ? "" : telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLockStatus() {
        return lockStatus == null ? "" : lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getNameAuthFlag() {
        return nameAuthFlag == null ? "" : nameAuthFlag;
    }

    public void setNameAuthFlag(String nameAuthFlag) {
        this.nameAuthFlag = nameAuthFlag;
    }

    public String getMemberSrc() {
        return memberSrc == null ? "" : memberSrc;
    }

    public void setMemberSrc(String memberSrc) {
        this.memberSrc = memberSrc;
    }

    public String getRecMobile() {
        return recMobile == null ? "" : recMobile;
    }

    public void setRecMobile(String recMobile) {
        this.recMobile = recMobile;
    }

    public String getRecCode() {
        return recCode == null ? "" : recCode;
    }

    public void setRecCode(String recCode) {
        this.recCode = recCode;
    }

    public String getHeadPath() {
        return headPath == null ? "" : headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getLastLoginTime() {
        return lastLoginTime == null ? "" : lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getCycleLoginFailTimes() {
        return cycleLoginFailTimes == null ? "" : cycleLoginFailTimes;
    }

    public void setCycleLoginFailTimes(String cycleLoginFailTimes) {
        this.cycleLoginFailTimes = cycleLoginFailTimes;
    }

    public String getIsBindMobile() {
        return isBindMobile == null ? "" : isBindMobile;
    }

    public void setIsBindMobile(String isBindMobile) {
        this.isBindMobile = isBindMobile;
    }

    public String getBindNo() {
        return bindNo == null ? "" : bindNo;
    }

    public void setBindNo(String bindNo) {
        this.bindNo = bindNo;
    }

    public String getPsignature() {
        return psignature == null ? "" : psignature;
    }

    public void setPsignature(String psignature) {
        this.psignature = psignature;
    }

    public String getAge() {
        return age == null ? "" : age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex == null ? "" : sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCounty() {
        return county == null ? "" : county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getProvince() {
        return province == null ? "" : province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city == null ? "" : city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area == null ? "" : area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBlacklist() {
        return blacklist == null ? "" : blacklist;
    }

    public void setBlacklist(String blacklist) {
        this.blacklist = blacklist;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getFloor() {
        return floor == null ? "" : floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getExtracts() {
        return extracts == null ? "" : extracts;
    }

    public void setExtracts(String extracts) {
        this.extracts = extracts;
    }
}
