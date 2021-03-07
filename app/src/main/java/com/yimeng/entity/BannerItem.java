package com.yimeng.entity;

import java.io.Serializable;

public class BannerItem implements Serializable {
    public String imgUrl;
    public String title;
    public String other;// 链接
    public String parentNo;// 1可以跳转

    public BannerItem() {
    }

    public BannerItem(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public BannerItem(String imgUrl, String title) {
        this.imgUrl = imgUrl;
        this.title = title;
    }

    public BannerItem(String imgUrl, String title, String other) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.other = other;
    }

    public BannerItem(String imgUrl, String title, String other, String parentNo) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.other = other;
        this.parentNo = parentNo;
    }

    public String getParentNo() {
        return parentNo;
    }

    public void setParentNo(String parentNo) {
        this.parentNo = parentNo;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
