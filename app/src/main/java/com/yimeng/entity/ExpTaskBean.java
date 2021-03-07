package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/25 10:48 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 体验任务
 * </pre>
 */
public class ExpTaskBean {

    /**
     * id : null
     * taskExpNo : null
     * taskType : null
     * title : 一起来玩儿体验任务吧
     * memberNo : null
     * merchantNo : null
     * orderNo : null
     * status : null
     * remark : null
     * createTime : null
     * updateTime : null
     * complete : 0
     * completeCount : 0
     */

    private Object id;
    private Object taskExpNo;
    private Object taskType;
    private String title;
    private Object memberNo;
    private Object merchantNo;
    private Object orderNo;
    private Object status;
    private Object remark;
    private Object createTime;
    private Object updateTime;
    private int complete;
    private int completeCount;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getTaskExpNo() {
        return taskExpNo;
    }

    public void setTaskExpNo(Object taskExpNo) {
        this.taskExpNo = taskExpNo;
    }

    public Object getTaskType() {
        return taskType;
    }

    public void setTaskType(Object taskType) {
        this.taskType = taskType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(Object memberNo) {
        this.memberNo = memberNo;
    }

    public Object getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(Object merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Object getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Object orderNo) {
        this.orderNo = orderNo;
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

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public int getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(int completeCount) {
        this.completeCount = completeCount;
    }
}
