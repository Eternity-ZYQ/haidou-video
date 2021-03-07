package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/23 0023 下午 09:01.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 亲子圈实体
 * </pre>
 */
public class ParentChildCircleDetail {

    private int id;
    /**
     * 说说编号
     */
    private String shuoshuoNo;
    /**
     * 说说类型
     */
    private String shuoshuoType;
    /**
     * 会员编号
     */
    private String memberNo;
    /**
     * 会员昵称
     */
    private String nickname;
    /**
     * 会员头像
     */
    private String headPath;
    /**
     * 内容
     */
    private String content;
    /**
     * 图片
     */
    private String images;
    private int isReprinted;
    private String reprintedUrl;
    private String country;
    private String province;
    private String city;
    private String area;
    private double longitude;
    private double latitude;
    private int isShowAddress;
    /**
     * 评论数量
     */
    private int commentNum;
    /**
     * 点赞数量
     */
    private int giveNum;
    private String status;
    private Object remark;
    private long createTime;
    private long updateTime;
    private Object fabulous;
    /**
     * 阅读数量
     */
    private int viewNum;
    /**
     * 是否点赞
     */
    private boolean memberFabulous;
    private String reprintedTitle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShuoshuoNo() {
        return shuoshuoNo;
    }

    public void setShuoshuoNo(String shuoshuoNo) {
        this.shuoshuoNo = shuoshuoNo;
    }

    public String getShuoshuoType() {
        return shuoshuoType;
    }

    public void setShuoshuoType(String shuoshuoType) {
        this.shuoshuoType = shuoshuoType;
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

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getIsReprinted() {
        return isReprinted;
    }

    public void setIsReprinted(int isReprinted) {
        this.isReprinted = isReprinted;
    }

    public String getReprintedUrl() {
        return reprintedUrl;
    }

    public void setReprintedUrl(String reprintedUrl) {
        this.reprintedUrl = reprintedUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getIsShowAddress() {
        return isShowAddress;
    }

    public void setIsShowAddress(int isShowAddress) {
        this.isShowAddress = isShowAddress;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getGiveNum() {
        return giveNum;
    }

    public void setGiveNum(int giveNum) {
        this.giveNum = giveNum;
    }

    public String getStatus() {
        return status;
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

    public Object getFabulous() {
        return fabulous;
    }

    public void setFabulous(Object fabulous) {
        this.fabulous = fabulous;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public boolean isMemberFabulous() {
        return memberFabulous;
    }

    public void setMemberFabulous(boolean memberFabulous) {
        this.memberFabulous = memberFabulous;
    }

    public String getReprintedTitle() {
        return reprintedTitle == null ? "" : reprintedTitle;
    }

    public void setReprintedTitle(String reprintedTitle) {
        this.reprintedTitle = reprintedTitle;
    }
}
