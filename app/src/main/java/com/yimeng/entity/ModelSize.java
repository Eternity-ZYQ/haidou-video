package com.yimeng.entity;

/**
 * Created by user on 2018/7/3.
 */

public class ModelSize {


    private String productSetNo;// 规格编号
    private String imgPath;//商品图片（多张用,分隔）
    private String size;//规格名称
    private String stock;//库存
    private String price;//价格
    private String supplyPrice;//供货价格
    private boolean sizeIsSelected;

    public ModelSize(String productSetNo, String imgPath, String size, String stock, String price, String supplyPrice) {
        this.productSetNo = productSetNo;
        this.imgPath = imgPath;
        this.size = size;
        this.stock = stock;
        this.price = price;
        this.supplyPrice = supplyPrice;
    }

    public boolean isSizeIsSelected() {
        return sizeIsSelected;
    }

    public void setSizeIsSelected(boolean sizeIsSelected) {
        this.sizeIsSelected = sizeIsSelected;
    }

    public String getProductSetNo() {
        return productSetNo;
    }

    public void setProductSetNo(String productSetNo) {
        this.productSetNo = productSetNo;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(String supplyPrice) {
        this.supplyPrice = supplyPrice;
    }
}
