package com.yimeng.entity;

/**
 * Created by user on 2018/6/28.
 * 维修订单评论详情 同 维修订单评价列表  同  商品评论分页列表
 */

public class ModelRepairOrderCommentDetail {

    private String id;
    private String commentNo;
    private String businessType;
    private String businessNo;
    private String memberNo;
    private String nickname;
    private String discriptScore;
    private String content;
    private String imgPath;
    private String createTime;
    private String updateTime;
    private String shopNo;
    private String headPath;

    public ModelRepairOrderCommentDetail(String id, String commentNo, String businessType, String businessNo, String memberNo, String nickname, String discriptScore, String content, String imgPath, String createTime, String updateTime, String shopNo, String headPath) {
        this.id = id;
        this.commentNo = commentNo;
        this.businessType = businessType;
        this.businessNo = businessNo;
        this.memberNo = memberNo;
        this.nickname = nickname;
        this.discriptScore = discriptScore;
        this.content = content;
        this.imgPath = imgPath;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.shopNo = shopNo;
        this.headPath = headPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentNo() {
        return commentNo;
    }

    public void setCommentNo(String commentNo) {
        this.commentNo = commentNo;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }
}
