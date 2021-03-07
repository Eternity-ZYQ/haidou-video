package com.yimeng.entity;

/**
 * Created by user on 2018/7/2.
 * 所有分类页面商品
 */

public class ModelProductCategoriesContent {
    private String id;//5066672
    private String logo;//图片地址
    private String menuNo;
    private String name;//电视机
    private String other;
    private String parentNo;
    private boolean isShopSale; // 店铺厂促商品

    public ModelProductCategoriesContent(){}
    public ModelProductCategoriesContent(String id, String logo, String menuNo, String name, String other, String parentNo) {
        this.id = id;
        this.logo = logo;
        this.menuNo = menuNo;
        this.name = name;
        this.other = other;
        this.parentNo = parentNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isShopSale() {
        return isShopSale;
    }

    public void setShopSale(boolean shopSale) {
        isShopSale = shopSale;
    }
}
