package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/17 0017 下午 03:19.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 联系客服实体
 * </pre>
 */
public class CustomerServiceBean {

    /**
     * id : 81222222212120
     * menuNo : 201805100001
     * menuType : touch
     * menuTypeName : touch
     * name : hotline
     * introduction : 400-8898-165
     * parentNo : null
     * sort : null
     * logo : /fileupload/shopImage/20170726/1501063345951(750x412).jpg
     * status : common
     * shopNo : null
     * createrAdminNo : null
     * other : null
     * createTime : 1538120657000
     * updateTime : 1538120657000
     * role : null
     * secondMenus : null
     * needImg : null
     */

    private long id;
    private String menuNo;
    private String menuType;
    private String menuTypeName;
    private String name;
    private String introduction;
    private Object parentNo;
    private Object sort;
    private String logo;
    private String status;
    private Object shopNo;
    private Object createrAdminNo;
    private Object other;
    private long createTime;
    private long updateTime;
    private Object role;
    private Object secondMenus;
    private Object needImg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMenuNo() {
        return menuNo == null ? "" : menuNo;
    }

    public void setMenuNo(String menuNo) {
        this.menuNo = menuNo;
    }

    public String getMenuType() {
        return menuType == null ? "" : menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuTypeName() {
        return menuTypeName == null ? "" : menuTypeName;
    }

    public void setMenuTypeName(String menuTypeName) {
        this.menuTypeName = menuTypeName;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction == null ? "" : introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Object getParentNo() {
        return parentNo;
    }

    public void setParentNo(Object parentNo) {
        this.parentNo = parentNo;
    }

    public Object getSort() {
        return sort;
    }

    public void setSort(Object sort) {
        this.sort = sort;
    }

    public String getLogo() {
        return logo == null ? "" : logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getShopNo() {
        return shopNo;
    }

    public void setShopNo(Object shopNo) {
        this.shopNo = shopNo;
    }

    public Object getCreaterAdminNo() {
        return createrAdminNo;
    }

    public void setCreaterAdminNo(Object createrAdminNo) {
        this.createrAdminNo = createrAdminNo;
    }

    public Object getOther() {
        return other;
    }

    public void setOther(Object other) {
        this.other = other;
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

    public Object getRole() {
        return role;
    }

    public void setRole(Object role) {
        this.role = role;
    }

    public Object getSecondMenus() {
        return secondMenus;
    }

    public void setSecondMenus(Object secondMenus) {
        this.secondMenus = secondMenus;
    }

    public Object getNeedImg() {
        return needImg;
    }

    public void setNeedImg(Object needImg) {
        this.needImg = needImg;
    }
}
