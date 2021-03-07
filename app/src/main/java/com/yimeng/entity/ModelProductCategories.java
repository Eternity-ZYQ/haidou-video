package com.yimeng.entity;

import java.util.LinkedList;

/**
 * Created by user on 2018/7/2.
 */

public class ModelProductCategories {

    private String id;
    private String menuNo;
    private String name;
    private String other;
    private String parentNo;
    private boolean isShopSale;// 店铺厂促啥商品
    private LinkedList<ModelProductCategoriesContent> modelProductCategoriesContentsList;

    private  int position;

    private boolean isSelected;

    public ModelProductCategories(){}

    public ModelProductCategories(String id, String menuNo, String name, String other, String parentNo, LinkedList<ModelProductCategoriesContent> modelProductCategoriesContentsList) {
        this.id = id;
        this.menuNo = menuNo;
        this.name = name;
        this.other = other;
        this.parentNo = parentNo;
        this.modelProductCategoriesContentsList = modelProductCategoriesContentsList;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LinkedList<ModelProductCategoriesContent> getModelProductCategoriesContentsList() {
        return modelProductCategoriesContentsList;
    }

    public void setModelProductCategoriesContentsList(LinkedList<ModelProductCategoriesContent> modelProductCategoriesContentsList) {
        this.modelProductCategoriesContentsList = modelProductCategoriesContentsList;
    }

    public boolean isShopSale() {
        return isShopSale;
    }

    public void setShopSale(boolean shopSale) {
        isShopSale = shopSale;
    }
}
