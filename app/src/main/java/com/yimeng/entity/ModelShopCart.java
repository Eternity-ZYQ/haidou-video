package com.yimeng.entity;

import java.util.LinkedList;

/**
 * hym
 * 购物车一级model
 */

public class ModelShopCart {

    private String shopNo;
    private String shopName;
    private String telephone;
    private String logoPath;
    private boolean shopIsSelected;
    private String logisticsFee;

    LinkedList<ModelShopCartGoods> modelShopCartGoodses;

    private int position;

    public ModelShopCart(String shopNo, String shopName, String telephone, String logoPath, LinkedList<ModelShopCartGoods> modelShopCartGoodses) {
        this.shopNo = shopNo;
        this.shopName = shopName;
        this.telephone = telephone;
        this.logoPath = logoPath;
        this.modelShopCartGoodses = modelShopCartGoodses;
    }

    public String getLogisticsFee() {
        return logisticsFee;
    }

    public void setLogisticsFee(String logisticsFee) {
        this.logisticsFee = logisticsFee;
    }

    public boolean isShopIsSelected() {
        return shopIsSelected;
    }

    public void setShopIsSelected(boolean shopIsSelected) {
        this.shopIsSelected = shopIsSelected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public LinkedList<ModelShopCartGoods> getModelShopCartGoodses() {
        return modelShopCartGoodses;
    }

    public void setModelShopCartGoodses(LinkedList<ModelShopCartGoods> modelShopCartGoodses) {
        this.modelShopCartGoodses = modelShopCartGoodses;
    }
}
