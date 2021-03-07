package com.yimeng.entity;

/**
 * @author lijb
 *         交易列表
 */

public class BusinessModel {

    private String orderNo; //订单编号"
    private String nickname; //昵称"
    private String orderName; //订单名称"
    private String memberHeadPath; //会员头像"
    private String realAmt; //实付交易金额"
    private String createTime; //创建时间"
    private String receiverMobile;// 手机号
    private int position;

    public BusinessModel(String orderNo, String nickname, String orderName, String memberHeadPath, String realAmt, String createTime) {
        this.orderNo = orderNo;
        this.nickname = nickname;
        this.orderName = orderName;
        this.memberHeadPath = memberHeadPath;
        this.realAmt = realAmt;
        this.createTime = createTime;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getMemberHeadPath() {
        return memberHeadPath;
    }

    public void setMemberHeadPath(String memberHeadPath) {
        this.memberHeadPath = memberHeadPath;
    }

    public String getRealAmt() {
        return realAmt;
    }

    public void setRealAmt(String realAmt) {
        this.realAmt = realAmt;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
