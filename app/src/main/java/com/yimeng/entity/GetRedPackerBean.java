package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/29 9:57 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class GetRedPackerBean {

    /**
     * id : 3
     * receiveNo : MR201908281837000363722
     * receiveType : getShareRedPack
     * projectNo : MP201908281104258736076
     * memberNo : WEB2019082818365954900000002
     * receiveAmt : 200
     * receiveBefore : 200
     * receiveAfter : 0
     * status : common
     * remark : null
     * createTime : 1566988624000
     * updateTime : 1566988624000
     * memberName : CX1566988619549
     * nickName : CX1566988619549
     * mobileNo : 15328301956
     * headPath : /fileupload/cuxiao/default.png
     * shopName : null
     * shopNo : null
     */

    private String receiveNo;
    private String receiveType;
    private String projectNo;
    private String memberNo;
    private int receiveAmt; // 单价
    private int receiveBefore;
    private int receiveAfter;
    private String status;
    private Object remark;
    private long createTime;
    private long updateTime;
    private String memberName;
    private String nickName;
    private String mobileNo;
    private String headPath;
    private String shopName;
    private String shopNo;

    public String getReceiveNo() {
        return receiveNo == null ? "" : receiveNo;
    }

    public void setReceiveNo(String receiveNo) {
        this.receiveNo = receiveNo;
    }

    public String getReceiveType() {
        return receiveType == null ? "" : receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public String getProjectNo() {
        return projectNo == null ? "" : projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getMemberNo() {
        return memberNo == null ? "" : memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public int getReceiveAmt() {
        return receiveAmt;
    }

    public void setReceiveAmt(int receiveAmt) {
        this.receiveAmt = receiveAmt;
    }

    public int getReceiveBefore() {
        return receiveBefore;
    }

    public void setReceiveBefore(int receiveBefore) {
        this.receiveBefore = receiveBefore;
    }

    public int getReceiveAfter() {
        return receiveAfter;
    }

    public void setReceiveAfter(int receiveAfter) {
        this.receiveAfter = receiveAfter;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
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
}
