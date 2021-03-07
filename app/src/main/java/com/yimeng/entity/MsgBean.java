package com.yimeng.entity;

import java.text.SimpleDateFormat;

/**
 * Author : huiGer
 * Time   : 2018/8/5 0005 下午 01:02.
 * Desc   :
 */
public class MsgBean {


    /**
     * 消息编号
     */
    private String messageNo;
    /**
     * 代理编号
     */
    private String agentNo;
    /**
     * 消息类型
     */
    private String messageType;
    private String messageTypeName;
    /**
     * 消息标题
     */
    private String messageTitle;
    /**
     * 消息作者
     */
    private String messageAuthor;
    /**
     * 消息内容
     */
    private String messageContent;
    /**
     * 消息图片
     */
    private String messageImage;
    /**
     * 接收者类型
     */
    private String receiverType;
    private String receiverTypeName;
    /**
     * 接收者编号
     */
    private String receiverNo;
    /**
     * 状态
     */
    private String status;
    /**
     * 创建时间
     */
    private long createTime;
    private long updateTime;

    public String getMessageNo() {
        return messageNo == null ? "" : messageNo;
    }

    public void setMessageNo(String messageNo) {
        this.messageNo = messageNo;
    }

    public String getAgentNo() {
        return agentNo == null ? "" : agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getMessageType() {
        return messageType == null ? "" : messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageTypeName() {
        return messageTypeName == null ? "" : messageTypeName;
    }

    public void setMessageTypeName(String messageTypeName) {
        this.messageTypeName = messageTypeName;
    }

    public String getMessageTitle() {
        return messageTitle == null ? "" : messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageAuthor() {
        return messageAuthor == null ? "" : messageAuthor;
    }

    public void setMessageAuthor(String messageAuthor) {
        this.messageAuthor = messageAuthor;
    }

    public String getMessageContent() {
        return messageContent == null ? "" : messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageImage() {
        return messageImage == null ? "" : messageImage;
    }

    public void setMessageImage(String messageImage) {
        this.messageImage = messageImage;
    }

    public String getReceiverType() {
        return receiverType == null ? "" : receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public String getReceiverTypeName() {
        return receiverTypeName == null ? "" : receiverTypeName;
    }

    public void setReceiverTypeName(String receiverTypeName) {
        this.receiverTypeName = receiverTypeName;
    }

    public String getReceiverNo() {
        return receiverNo == null ? "" : receiverNo;
    }

    public void setReceiverNo(String receiverNo) {
        this.receiverNo = receiverNo;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(createTime);
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
}
