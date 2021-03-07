package com.yimeng.entity;

import java.io.Serializable;

/**
 * 结算
 *
 * @author xp
 * @describe 结算.
 * @date 2018/6/23.
 */

public class ModelSettlement implements Serializable {
    private String shopN0;//店铺NO
    private String shopName;//店铺名称
    private String productNo;//商品编号
    private String productName;//商品名称
    private String count;//购买数量
    private String insuranceFee;//保险费金额
    private String income;//补贴麻豆，分做单位
    private String imagesPath;//商品图片
    private String price;//商品价格
    private String logisticsType;//支持配送 all/take

    public ModelSettlement(String shopName, String productNo, String productName, String count, String insuranceFee, String income, String imagesPath, String price) {
        this.shopName = shopName;
        this.productNo = productNo;
        this.productName = productName;
        this.count = count;
        this.insuranceFee = insuranceFee;
        this.income = income;
        this.imagesPath = imagesPath;
        this.price = price;
    }

    public String getShopN0() {
        return shopN0 == null ? "" : shopN0;
    }

    public void setShopN0(String shopN0) {
        this.shopN0 = shopN0;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(String insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getImagesPath() {
        String logoPath = "";
        if (imagesPath != null) {
            String[] imgs = imagesPath.split(",");
            logoPath = imgs.length > 0 ? imgs[0] : imagesPath;
        }

        return logoPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLogisticsType() {
        return logisticsType;
    }

    public void setLogisticsType(String logisticsType) {
        this.logisticsType = logisticsType;
    }
}
