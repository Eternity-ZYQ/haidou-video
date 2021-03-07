package com.yimeng.entity;

import java.io.Serializable;

/**
 * Created by user on 2018/6/28.
 * 热卖商品
 */

public class ModelGetHotSalesProduct implements Serializable {

    private String area;
    private String city;
    private String collect;
    private String createTime;
    private String customService;
    private String description;
    private String detailImgPath;
    private String discountRatio;
    private String flag;
    private String freightage;
    private String gradeTimes;
    private String hasSaled;
    private String id;
    private String imagesPath;
    private String isOnsale;
    private String jsonProductSetBean;
    private String logisticsType;
    private String memberNo;
    private String menuNo;
    private String orderNo;
    private String originalPrice;
    private String price;

    private String productBanner;
    private String productCategoryNo;
    private String productName;
    private String productNo;
    private String productNum;
    private String productParams;
    private String productSetList;
    private String productTitle;
    private String productType;
    private String productsNos;
    private String province;
    private String remark;
    private String shopCarNum;
    private String shopClassify;
    private String shopName;
    private String shopNo;
    private String sort;
    private String status;
    private String storage;
    private String telephone;
    private String totalGrade;
    private String totalSaleAmt;
    private String units;
    private String updateTime;
    private String vipPrice;
    private String income;

    public ModelGetHotSalesProduct(String area, String city, String collect, String createTime, String customService, String description, String detailImgPath, String discountRatio, String flag, String freightage, String gradeTimes, String hasSaled, String id, String imagesPath, String isOnsale, String jsonProductSetBean, String logisticsType, String memberNo, String menuNo, String orderNo, String originalPrice, String price, String productBanner, String productCategoryNo, String productName, String productNo, String productNum, String productParams, String productSetList, String productTitle, String productType, String productsNos, String province, String remark, String shopCarNum, String shopClassify, String shopName, String shopNo, String sort, String status, String storage, String telephone, String totalGrade, String totalSaleAmt, String units, String updateTime, String vipPrice) {
        this.area = area;
        this.city = city;
        this.collect = collect;
        this.createTime = createTime;
        this.customService = customService;
        this.description = description;
        this.detailImgPath = detailImgPath;
        this.discountRatio = discountRatio;
        this.flag = flag;
        this.freightage = freightage;
        this.gradeTimes = gradeTimes;
        this.hasSaled = hasSaled;
        this.id = id;
        this.imagesPath = imagesPath;
        this.isOnsale = isOnsale;
        this.jsonProductSetBean = jsonProductSetBean;
        this.logisticsType = logisticsType;
        this.memberNo = memberNo;
        this.menuNo = menuNo;
        this.orderNo = orderNo;
        this.originalPrice = originalPrice;
        this.price = price;
        this.productBanner = productBanner;
        this.productCategoryNo = productCategoryNo;
        this.productName = productName;
        this.productNo = productNo;
        this.productNum = productNum;
        this.productParams = productParams;
        this.productSetList = productSetList;
        this.productTitle = productTitle;
        this.productType = productType;
        this.productsNos = productsNos;
        this.province = province;
        this.remark = remark;
        this.shopCarNum = shopCarNum;
        this.shopClassify = shopClassify;
        this.shopName = shopName;
        this.shopNo = shopNo;
        this.sort = sort;
        this.status = status;
        this.storage = storage;
        this.telephone = telephone;
        this.totalGrade = totalGrade;
        this.totalSaleAmt = totalSaleAmt;
        this.units = units;
        this.updateTime = updateTime;
        this.vipPrice = vipPrice;
    }

    public String getIncome() {
        return income == null ? "" : income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCustomService() {
        return customService;
    }

    public void setCustomService(String customService) {
        this.customService = customService;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetailImgPath() {
        return detailImgPath;
    }

    public void setDetailImgPath(String detailImgPath) {
        this.detailImgPath = detailImgPath;
    }

    public String getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(String discountRatio) {
        this.discountRatio = discountRatio;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFreightage() {
        return freightage;
    }

    public void setFreightage(String freightage) {
        this.freightage = freightage;
    }

    public String getGradeTimes() {
        return gradeTimes;
    }

    public void setGradeTimes(String gradeTimes) {
        this.gradeTimes = gradeTimes;
    }

    public String getHasSaled() {
        return hasSaled;
    }

    public void setHasSaled(String hasSaled) {
        this.hasSaled = hasSaled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public String getIsOnsale() {
        return isOnsale;
    }

    public void setIsOnsale(String isOnsale) {
        this.isOnsale = isOnsale;
    }

    public String getJsonProductSetBean() {
        return jsonProductSetBean;
    }

    public void setJsonProductSetBean(String jsonProductSetBean) {
        this.jsonProductSetBean = jsonProductSetBean;
    }

    public String getLogisticsType() {
        return logisticsType;
    }

    public void setLogisticsType(String logisticsType) {
        this.logisticsType = logisticsType;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(String menuNo) {
        this.menuNo = menuNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductBanner() {
        return productBanner;
    }

    public void setProductBanner(String productBanner) {
        this.productBanner = productBanner;
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

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getProductParams() {
        return productParams;
    }

    public void setProductParams(String productParams) {
        this.productParams = productParams;
    }

    public String getProductSetList() {
        return productSetList;
    }

    public void setProductSetList(String productSetList) {
        this.productSetList = productSetList;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductsNos() {
        return productsNos;
    }

    public void setProductsNos(String productsNos) {
        this.productsNos = productsNos;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShopCarNum() {
        return shopCarNum;
    }

    public void setShopCarNum(String shopCarNum) {
        this.shopCarNum = shopCarNum;
    }

    public String getShopClassify() {
        return shopClassify;
    }

    public void setShopClassify(String shopClassify) {
        this.shopClassify = shopClassify;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTotalGrade() {
        return totalGrade;
    }

    public void setTotalGrade(String totalGrade) {
        this.totalGrade = totalGrade;
    }

    public String getTotalSaleAmt() {
        return totalSaleAmt;
    }

    public void setTotalSaleAmt(String totalSaleAmt) {
        this.totalSaleAmt = totalSaleAmt;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }
}
