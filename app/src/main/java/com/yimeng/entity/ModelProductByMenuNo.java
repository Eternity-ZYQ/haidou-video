package com.yimeng.entity;

/**
 * Created by user on 2018/7/2.
 */

public class ModelProductByMenuNo {
    private String productNo;
    private String productName;
    private String imagesPath;
    private String description;
    private String price;
    private String units;
    private String productCategoryNo;
    private String shopNo;
    private String collect;
    private String hasSaled;
    private String income;
    private boolean isChecked;

    public ModelProductByMenuNo(){}

    public ModelProductByMenuNo(String productNo, String productName, String imagesPath, String description, String price, String units, String productCategoryNo, String shopNo, String collect, String hasSaled) {
        this.productNo = productNo;
        this.productName = productName;
        this.imagesPath = imagesPath;
        this.description = description;
        this.price = price;
        this.units = units;
        this.productCategoryNo = productCategoryNo;
        this.shopNo = shopNo;
        this.collect = collect;
        this.hasSaled = hasSaled;
    }

    public String getHasSaled() {
        return hasSaled;
    }

    public void setHasSaled(String hasSaled) {
        this.hasSaled = hasSaled;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getProductCategoryNo() {
        return productCategoryNo;
    }

    public void setProductCategoryNo(String productCategoryNo) {
        this.productCategoryNo = productCategoryNo;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getIncome() {
        return income == null ? "" : income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
