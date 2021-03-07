package com.yimeng.entity;

/**
 * Created by user on 2018/6/30.
 * 商品搜索结果
 */

public class ModelSearchProduct {

    private String productNo;
    private String shopNo;
    private String shopName;
    private String menuNo;
    private String productCategoryNo;
    private String productName;
    private String productType;
    private String price;
    private String units;
    private String storage;
    private String hasSaled;
    private String totalGrade;
    private String gradeTimes;
    private String imagesPath;//商品图片地址（多张用,分隔 第一张未logo）
    private String detailImgPath;

    public ModelSearchProduct(String productNo, String shopNo, String shopName, String menuNo, String productCategoryNo, String productName, String productType, String price, String units, String storage, String hasSaled, String totalGrade, String gradeTimes, String imagesPath, String detailImgPath) {
        this.productNo = productNo;
        this.shopNo = shopNo;
        this.shopName = shopName;
        this.menuNo = menuNo;
        this.productCategoryNo = productCategoryNo;
        this.productName = productName;
        this.productType = productType;
        this.price = price;
        this.units = units;
        this.storage = storage;
        this.hasSaled = hasSaled;
        this.totalGrade = totalGrade;
        this.gradeTimes = gradeTimes;
        this.imagesPath = imagesPath;
        this.detailImgPath = detailImgPath;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(String menuNo) {
        this.menuNo = menuNo;
    }

    public String getProductCategoryNo() {
        return productCategoryNo;
    }

    public void setProductCategoryNo(String productCategoryNo) {
        this.productCategoryNo = productCategoryNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getHasSaled() {
        return hasSaled;
    }

    public void setHasSaled(String hasSaled) {
        this.hasSaled = hasSaled;
    }

    public String getTotalGrade() {
        return totalGrade;
    }

    public void setTotalGrade(String totalGrade) {
        this.totalGrade = totalGrade;
    }

    public String getGradeTimes() {
        return gradeTimes;
    }

    public void setGradeTimes(String gradeTimes) {
        this.gradeTimes = gradeTimes;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public String getDetailImgPath() {
        return detailImgPath;
    }

    public void setDetailImgPath(String detailImgPath) {
        this.detailImgPath = detailImgPath;
    }
}
