package com.yimeng.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/3/23 0023 下午 06:28.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 任务实体
 * </pre>
 */
public class TaskBean {

    private String headName;
    private String title;
    private Object id;
    private String taskNo;
    private Object taskType;
    private int taskGrade;
    private String taskGradeAlias;
    private Object targetGrade;
    private String description;
    private Object memberNo;
    private Object status;
    private Object remark;
    private Object createTime;
    private Object updateTime;
    private Object adUrl;
    private Object adImg;
    private Object adType;
    private Object clickNum;
    // 销售总进度
    private int lowSales;
    // 销售当前任务
    private int hasSales;
    // 推荐总进度
    private int lowRecommend;
    // 推荐当前进度
    private int hasRecommend;
    private int complete; // 0, 进行中, 1. 已完成
    private Object taskItemCount;
    private Object completeCount;
    private String currentIncome;
    private String expectIncome;
    private boolean isExpanded;
    private List<TaskItemBean> children;
    // 区分任务布局
    private int itemType;
    private boolean isTaskNew;

    public TaskBean() {
    }

    public TaskBean(int itemType) {
        this.itemType = itemType;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getHeadName() {
        return headName == null ? "" : headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getTaskNo() {
        return taskNo == null ? "" : taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public Object getTaskType() {
        return taskType;
    }

    public void setTaskType(Object taskType) {
        this.taskType = taskType;
    }

    public int getTaskGrade() {
        return taskGrade;
    }

    public void setTaskGrade(int taskGrade) {
        this.taskGrade = taskGrade;
    }

    public String getTaskGradeAlias() {
        return taskGradeAlias == null ? "" : taskGradeAlias;
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

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(Object memberNo) {
        this.memberNo = memberNo;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public Object getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(Object adUrl) {
        this.adUrl = adUrl;
    }

    public Object getAdImg() {
        return adImg;
    }

    public void setAdImg(Object adImg) {
        this.adImg = adImg;
    }

    public Object getAdType() {
        return adType;
    }

    public void setAdType(Object adType) {
        this.adType = adType;
    }

    public Object getClickNum() {
        return clickNum;
    }

    public void setClickNum(Object clickNum) {
        this.clickNum = clickNum;
    }

    public int getLowSales() {
        return lowSales;
    }

    public void setLowSales(int lowSales) {
        this.lowSales = lowSales;
    }

    public int getHasSales() {
        return hasSales;
    }

    public void setHasSales(int hasSales) {
        this.hasSales = hasSales;
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

    public Object getTaskItemCount() {
        return taskItemCount;
    }

    public void setTaskItemCount(Object taskItemCount) {
        this.taskItemCount = taskItemCount;
    }

    public Object getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(Object completeCount) {
        this.completeCount = completeCount;
    }

    public String getCurrentIncome() {
        return currentIncome == null ? "" : currentIncome;
    }

    public void setCurrentIncome(String currentIncome) {
        this.currentIncome = currentIncome;
    }

    public String getExpectIncome() {
        return expectIncome == null ? "" : expectIncome;
    }

    public void setExpectIncome(String expectIncome) {
        this.expectIncome = expectIncome;
    }

    public List<TaskItemBean> getChildren() {
        if (children == null) {
            return children = new ArrayList<>();
        }
        return children;
    }

    public void setChildren(List<TaskItemBean> children) {
        this.children = children;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isTaskNew() {
        return isTaskNew;
    }

    public void setTaskNew(boolean taskNew) {
        isTaskNew = taskNew;
    }
}
