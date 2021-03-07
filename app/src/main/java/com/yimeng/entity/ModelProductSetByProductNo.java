package com.yimeng.entity;

import java.util.LinkedList;

/**
 * Created by user on 2018/6/28.
 * 商品款式规格
 */

public class ModelProductSetByProductNo {

    private String productSetNo;//规格编号
    private String productNo;//产品编号
    private String color;//一级规格名称
    private boolean colorIsSelected;

    private LinkedList<ModelSize> sizeList;

    public ModelProductSetByProductNo(String productSetNo, String productNo, String color, LinkedList<ModelSize> sizeList) {
        this.productSetNo = productSetNo;
        this.productNo = productNo;
        this.color = color;
        this.sizeList = sizeList;
    }

    public boolean isColorIsSelected() {
        return colorIsSelected;
    }

    public void setColorIsSelected(boolean colorIsSelected) {
        this.colorIsSelected = colorIsSelected;
    }

    public String getProductSetNo() {
        return productSetNo;
    }

    public void setProductSetNo(String productSetNo) {
        this.productSetNo = productSetNo;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LinkedList<ModelSize> getSizeList() {
        return sizeList;
    }

    public void setSizeList(LinkedList<ModelSize> sizeList) {
        this.sizeList = sizeList;
    }
}
