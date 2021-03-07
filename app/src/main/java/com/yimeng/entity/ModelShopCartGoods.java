package com.yimeng.entity;

/**
 * Created by user on 2018/7/2.
 * 购物车二级model
 */

public class ModelShopCartGoods {

    private String productNo;//商品编号
    private String menuNo;//分类编号
    private String productName;//名称
    private String price; //价格（麻豆为价格的一半）
    private String shopCarNum;// 数量
    private String sort;//库存
    private String remark;//颜色，尺码等
    private String imagesPath;//
    private String shopCarNo;//;购物车编号

    private int position;// 位置
    private int fPosition;// 位置
    private boolean productIsSelected;//是否已选

    private int mStatus;// 1添加,2减少
    private String mShopNo;
    private String isOnsale; // 上架状态

    public ModelShopCartGoods(String productNo, String menuNo, String productName, String price, String shopCarNum, String sort, String remark, String imagesPath, String shopCarNo) {
        this.productNo = productNo;
        this.menuNo = menuNo;
        this.productName = productName;
        this.price = price;
        this.shopCarNum = shopCarNum;
        this.sort = sort;
        this.remark = remark;
        this.imagesPath = imagesPath;
        this.shopCarNo = shopCarNo;
    }

    public String getIsOnsale() {
        return isOnsale == null ? "" : isOnsale;
    }

    public void setIsOnsale(String isOnsale) {
        this.isOnsale = isOnsale;
    }

    public String getmShopNo() {
        return mShopNo;
    }

    public void setmShopNo(String mShopNo) {
        this.mShopNo = mShopNo;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public boolean isProductIsSelected() {
        return productIsSelected;
    }

    public void setProductIsSelected(boolean productIsSelected) {
        this.productIsSelected = productIsSelected;
    }

    public String getShopCarNo() {
        return shopCarNo;
    }

    public void setShopCarNo(String shopCarNo) {
        this.shopCarNo = shopCarNo;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getfPosition() {
        return fPosition;
    }

    public void setfPosition(int fPosition) {
        this.fPosition = fPosition;
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
}
