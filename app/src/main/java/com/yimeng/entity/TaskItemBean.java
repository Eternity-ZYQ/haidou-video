package com.yimeng.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/3/23 0023 下午 06:28.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 任务实体
 * </pre>
 */
public class TaskItemBean implements MultiItemEntity{


    /**
     * 布局标识
     */
    private int itemType;
    private int id;
    private String taskNo;
    private String taskType;
    private Object taskGrade;
    private String taskGradeAlias;
    private Object targetGrade;
    private String title;
    private String description;
    private String status;
    private String remark;
    private long createTime;
    private long updateTime;
    private String adUrl;
    private String adImg;
    private String adType;
    private int clickNum;
    private String taskItemNo;
    private String memberNo;
    private String shopNo;
    private String merchantNo;
    private String shopName;
    // 总进度
    private int lowRecommend;
    // 当前进度
    private int hasRecommend;
    // 完成进度
    private int complete;


    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Object getTaskGrade() {
        return taskGrade;
    }

    public void setTaskGrade(Object taskGrade) {
        this.taskGrade = taskGrade;
    }

    public String getTaskGradeAlias() {
        return taskGradeAlias;
    }

    public void setTaskGradeAlias(String taskGradeAlias) {
        this.taskGradeAlias = taskGradeAlias;
    }

    public Object getTargetGrade() {
        return targetGrade;
    }

    public void setTargetGrade(Object targetGrade) {
        this.targetGrade = targetGrade;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
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

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getAdImg() {
        return adImg;
    }

    public void setAdImg(String adImg) {
        this.adImg = adImg;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }

    public String getTaskItemNo() {
        return taskItemNo == null ? "" : taskItemNo;
    }

    public void setTaskItemNo(String taskItemNo) {
        this.taskItemNo = taskItemNo;
    }

    public String getShopNo() {
        return shopNo == null ? "" : shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getMerchantNo() {
        return merchantNo == null ? "" : merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getShopName() {
        return shopName == null ? "" : shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getLowRecommend() {
        return lowRecommend;
    }

    public void setLowRecommend(int lowRecommend) {
        this.lowRecommend = lowRecommend;
    }

    public int getHasRecommend() {
        return hasRecommend;
    }

    public void setHasRecommend(int hasRecommend) {
        this.hasRecommend = hasRecommend;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }
}
