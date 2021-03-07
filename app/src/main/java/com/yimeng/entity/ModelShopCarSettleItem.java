package com.yimeng.entity;

import java.io.Serializable;

/**
 * 立即购买页面商品model
 * 二级model
 */

public class ModelShopCarSettleItem implements Serializable {

    String productNo;//商品编号
    String menuNo;//分类编号
    String productName;//名称
    String price;//价格（麻豆为价格的一半）
    String vipPrice;//麻豆折扣价
    String shopCarNum;//数量
    String sort;//库存
    String remark;//颜色，尺码等
    String imagesPath;//图片路径（多张用,分隔）
    String freightage;//运费
    String shopCarNo;//购物车编号

    public ModelShopCarSettleItem(String productNo, String menuNo, String productName, String price, String vipPrice, String shopCarNum, String sort, String remark, String imagesPath, String freightage) {
        this.productNo = productNo;
        this.menuNo = menuNo;
        this.productName = productName;
        this.price = price;
        this.vipPrice = vipPrice;
        this.shopCarNum = shopCarNum;
        this.sort = sort;
        this.remark = remark;
        this.imagesPath = imagesPath;
        this.freightage = freightage;
    }

    public String getShopCarNo() {
        return shopCarNo;
    }

    public void setShopCarNo(String shopCarNo) {
        this.shopCarNo = shopCarNo;
    }

    public String getFreightage() {
        return freightage;
    }

    public void setFreightage(String freightage) {
        this.freightage = freightage;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(String menuNo) {
        this.menuNo = menuNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

    public String getShopCarNum() {
        return shopCarNum;
    }

    public void setShopCarNum(String shopCarNum) {
        this.shopCarNum = shopCarNum;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }
}
