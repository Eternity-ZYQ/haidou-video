package com.yimeng.entity;

/**
 * Created by user on 2018/6/28.
 * 商城首页轮播图
 */

public class ModelGetBannerList {

    private String createTime;
    private String createrAdminNo;
    private String id;
    private String introduction;
    private String logo;
    private String menuNo;
    private String menuType;
    private String menuTypeName;
    private String name;
    private String other;
    private String parentNo;
    private String role;
    private String shopNo;
    private String sort;
    private String status;
    private String updateTime;

    public ModelGetBannerList(String createTime, String createrAdminNo, String id, String introduction, String logo, String menuNo, String menuType, String menuTypeName, String name, String other, String parentNo, String role, String shopNo, String sort, String status, String updateTime) {
        this.createTime = createTime;
        this.createrAdminNo = createrAdminNo;
        this.id = id;
        this.introduction = introduction;
        this.logo = logo;
        this.menuNo = menuNo;
        this.menuType = menuType;
        this.menuTypeName = menuTypeName;
        this.name = name;
        this.other = other;
        this.parentNo = parentNo;
        this.role = role;
        this.shopNo = shopNo;
        this.sort = sort;
        this.status = status;
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreaterAdminNo() {
        return createrAdminNo;
    }

    public void setCreaterAdminNo(String createrAdminNo) {
        this.createrAdminNo = createrAdminNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(String menuNo) {
        this.menuNo = menuNo;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuTypeName() {
        return menuTypeName;
    }

    public void setMenuTypeName(String menuTypeName) {
        this.menuTypeName = menuTypeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getParentNo() {
        return parentNo;
    }

    public void setParentNo(String parentNo) {
        this.parentNo = parentNo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
