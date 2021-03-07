package com.yimeng.entity;

import java.util.LinkedList;

/**
 * Created by user on 2018/6/28.
 * 购物车结算页面商品信息
 */

public class ModelShopCarSettle {

    private String shopNo;//商铺编号
    private String shopName;//商品名称
    private String telephone;//商铺联系方式
    private String logoPath;//logo
    private String logisticsFee;// 店铺运费
    private String leaveMsg;// 用户备注信息
    private boolean canInvoice; // 是否可开发票

    private LinkedList<ModelShopCarSettleItem> modelShopCarSettleItemList;

    public ModelShopCarSettle(String shopNo, String shopName, String telephone, String logoPath,
                              boolean canInvoice, LinkedList<ModelShopCarSettleItem> modelShopCarSettleItemList) {
        this.shopNo = shopNo;
        this.shopName = shopName;
        this.telephone = telephone;
        this.logoPath = logoPath;
        this.canInvoice = canInvoice;
        this.modelShopCarSettleItemList = modelShopCarSettleItemList;
    }

    public String getLeaveMsg() {
        return leaveMsg;
    }

    public void setLeaveMsg(String leaveMsg) {
        this.leaveMsg = leaveMsg;
    }

    public String getLogisticsFee() {
        return logisticsFee;
    }

    public void setLogisticsFee(String logisticsFee) {
        this.logisticsFee = logisticsFee;
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

    public boolean isCanInvoice() {
        return canInvoice;
    }

    public void setCanInvoice(boolean canInvoice) {
        this.canInvoice = canInvoice;
    }

    public LinkedList<ModelShopCarSettleItem> getModelShopCarSettleItemList() {
        return modelShopCarSettleItemList;
    }

    public void setModelShopCarSettleItemList(LinkedList<ModelShopCarSettleItem> modelShopCarSettleItemList) {
        this.modelShopCarSettleItemList = modelShopCarSettleItemList;
    }
}
