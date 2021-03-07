/*
 * ************************************************
 * 文件：ArticleBean.java
 * Author：huiGer
 * 当前修改时间：2019年04月27日 11:05:31
 * 上次修改时间：2019年04月27日 11:05:30
 *
 * Copyright (c) 2019.
 * ************************************************
 */

package com.yimeng.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/4/27 11:05 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 头条文章实体
 * </pre>
 */
public class ArticleBean implements MultiItemEntity {

    /**
     * id : 16
     * articleNo : 201904221205499753496
     * articleTitle : 形形色色是
     * articleImg : /fileupload/shopImage/20190422/1555905941423899270119(637x431).jpg
     * articleContent : 啊飒飒大是多撒多所撒多所大多撒
     * menuNo : parentChildKnowledge
     * articleAuthor : 超级管理员
     * authorNo : AD1538987209290
     * projectNo : null
     * tribeNo : null
     * remark : 1
     * status : common
     * price : 1
     * readNum : null
     * likeNum : null
     * createTime : 1555905950000
     * updateTime : 1555905950000
     * commission : 1
     * reward : 0
     */

    private int id;
    private String articleNo;
    private String articleTitle;
    private String articleImg;
    private String articleContent;
    private String menuNo;
    private String articleAuthor;
    private String authorNo;
    private Object projectNo;
    private Object tribeNo;
    private String remark;
    private String status;
    private int price;
    private Object readNum;
    private Object likeNum;
    private long createTime;
    private long updateTime;
    private int commission;
    private String reward;
    /**
     * 布局标识
     */
    private int itemType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArticleNo() {
        return articleNo;
    }

    public void setArticleNo(String articleNo) {
        this.articleNo = articleNo;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleImg() {
        return articleImg;
    }

    public void setArticleImg(String articleImg) {
        this.articleImg = articleImg;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public String getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(String menuNo) {
        this.menuNo = menuNo;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public String getAuthorNo() {
        return authorNo;
    }

    public void setAuthorNo(String authorNo) {
        this.authorNo = authorNo;
    }

    public Object getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(Object projectNo) {
        this.projectNo = projectNo;
    }

    public Object getTribeNo() {
        return tribeNo;
    }

    public void setTribeNo(Object tribeNo) {
        this.tribeNo = tribeNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Object getReadNum() {
        return readNum;
    }

    public void setReadNum(Object readNum) {
        this.readNum = readNum;
    }

    public Object getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Object likeNum) {
        this.likeNum = likeNum;
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

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
