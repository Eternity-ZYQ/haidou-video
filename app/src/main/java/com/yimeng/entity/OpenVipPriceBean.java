package com.yimeng.entity;

import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/29 11:16 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 会员资费介绍
 * </pre>
 */
public class OpenVipPriceBean {


    private List<VipTypeBean> vipType;
    private List<VipRewardArticleBean> vipRewardArticle;

    public List<VipTypeBean> getVipType() {
        return vipType;
    }

    public void setVipType(List<VipTypeBean> vipType) {
        this.vipType = vipType;
    }

    public List<VipRewardArticleBean> getVipRewardArticle() {
        return vipRewardArticle;
    }

    public void setVipRewardArticle(List<VipRewardArticleBean> vipRewardArticle) {
        this.vipRewardArticle = vipRewardArticle;
    }

    public static class VipTypeBean {
        /**
         * id : 77889901
         * menuNo : monthVip
         * menuType : vipType
         * menuTypeName : vipType
         * name : 月会员
         * introduction : ￥20/月
         * parentNo : 仅需0.67元/天
         * sort : 1
         * logo :
         * status : common
         * shopNo : null
         * createrAdminNo : AD1538987209290
         * other : 2000
         * createTime : 1561713105000
         * updateTime : 1561713105000
         * role : null
         * secondMenus : null
         * needImg : null
         * data : null
         */

        private int id;
        private String menuNo;
        private String menuType;
        private String menuTypeName;
        private String name;
        private String introduction;
        private String parentNo;
        private int sort;
        private String logo;
        private String status;
        private Object shopNo;
        private String createrAdminNo;
        private String other;
        private long createTime;
        private long updateTime;
        private Object role;
        private Object secondMenus;
        private Object needImg;
        private Object data;
        private boolean isChecked;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getParentNo() {
            return parentNo;
        }

        public void setParentNo(String parentNo) {
            this.parentNo = parentNo;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getStatus() {
            return status;
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

        public String getCreaterAdminNo() {
            return createrAdminNo;
        }

        public void setCreaterAdminNo(String createrAdminNo) {
            this.createrAdminNo = createrAdminNo;
        }

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
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

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }

    public static class VipRewardArticleBean {
        /**
         * id : 77889900
         * menuNo : 201906281711450893086
         * menuType : vipRewardArticle
         * menuTypeName : vipRewardArticle
         * name : vip权益1
         * introduction : vip权益1
         * parentNo : null
         * sort : 1
         * logo :
         * status : common
         * shopNo : null
         * createrAdminNo : AD1538987209290
         * other : 2
         * createTime : 1561713105000
         * updateTime : 1561713105000
         * role : null
         * secondMenus : null
         * needImg : null
         * data : null
         */

        private int id;
        private String menuNo;
        private String menuType;
        private String menuTypeName;
        private String name;
        private String introduction;
        private Object parentNo;
        private int sort;
        private String logo;
        private String status;
        private Object shopNo;
        private String createrAdminNo;
        private String other;
        private long createTime;
        private long updateTime;
        private Object role;
        private Object secondMenus;
        private Object needImg;
        private Object data;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getIntroduction() {
            return introduction;
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

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getStatus() {
            return status;
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

        public String getCreaterAdminNo() {
            return createrAdminNo;
        }

        public void setCreaterAdminNo(String createrAdminNo) {
            this.createrAdminNo = createrAdminNo;
        }

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
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

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
