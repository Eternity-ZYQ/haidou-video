package com.yimeng.entity;

import java.io.Serializable;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/7 2:37 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 任务价格专区
 * </pre>
 */
public class TaskBlockBean implements Serializable {

    private static final long serialVersionUID = 6983740489677601787L;
    private int id;
    private String blockNo;
    // 区块名称
    private String blockName;
    // 区块标语
    private String blockTitle;
    // 区块介绍
    private String blockIntroduct;
    // 区块图片
    private String blockImage;
    // 区块价格(分)
    private String blockPrice;
    // 有效标志
    private String status;
    // 备注
    private String remark;
    private long createTime;
    private long updateTime;
    // 是否开启了区块  =1 已激活
    private int isOpen;
    // 消耗种子数量
    private int seedNum;

    public int getSeedNum() {
        return seedNum;
    }

    public void setSeedNum(int seedNum) {
        this.seedNum = seedNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBlockNo() {
        return blockNo == null ? "" : blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public String getBlockName() {
        return blockName == null ? "" : blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getBlockTitle() {
        return blockTitle == null ? "" : blockTitle;
    }

    public void setBlockTitle(String blockTitle) {
        this.blockTitle = blockTitle;
    }

    public String getBlockIntroduct() {
        return blockIntroduct == null ? "" : blockIntroduct;
    }

    public void setBlockIntroduct(String blockIntroduct) {
        this.blockIntroduct = blockIntroduct;
    }

    public String getBlockImage() {
        return blockImage == null ? "" : blockImage;
    }

    public void setBlockImage(String blockImage) {
        this.blockImage = blockImage;
    }

    public String getBlockPrice() {
        return blockPrice;
    }

    public void setBlockPrice(String blockPrice) {
        this.blockPrice = blockPrice;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }
}
