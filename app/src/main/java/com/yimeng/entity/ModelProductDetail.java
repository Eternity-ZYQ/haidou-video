package com.yimeng.entity;

import java.util.LinkedList;

/**
 * Created by user on 2018/6/28.
 * 商品详情
 */

public class ModelProductDetail {

    private String id;
    private String productNo; // 商品编号
    private String productsNos;// 多个商品编号，以逗号隔开
    private String shopNo;    //店铺编号
    private String shopName;   //店铺名称
    private String menuNo;    //分类编号
    private String productCategoryNo;  //商家分类编号
    private String productName;  //商品名称
    private String productType; //商品类型
    private String price;     //价格 （用这个）
    private String originalPrice; //市场价
    private String vipPrice;   //会员价
    private String shopCarNum;  //购物车数量
    private String units;   //单位
    private String storage;  //库存
    private String hasSaled;  //已卖出
    private String imagesPath; //
    private String description;  //描述
    private String isOnsale;  //是否上架
    private String remark;   //
    private String status;   //状态
    private String createTime;
    private String updateTime;
    private String sort;   //排序
    private String detailImgPath;  //商品详情图片
    private String productBanner;
    private LinkedList<ModelProductParams> productParamsList; //商品参数 废弃
    private String productTitle;
    private String discount; //是否有折扣
    private String discountRatio; //折扣比例
    private String shopClassify;//店铺分类
    private String freightage; //运费
    private String customService;//售后保障
    private String orderNo;
    private String productNum;//商品数量
    private String flag;//商品标示
    private String collect; //是否收藏
    private String totalGrade;
    private String productParams;
    private boolean unChecked;
    private String income;
    /**
     * 选择的数量
     */
    private int selectCount;
    /**
     * 是否为平台导入的商品
     */
    private String productPlatNo;
    private int feeMode;//商品服务类型   0。线上（仅支持送货）1。到店（仅支持到店消费）2。都支持

    public ModelProductDetail(String id, String productNo, String productsNos, String shopNo, String shopName, String menuNo, String productCategoryNo, String productName, String productType, String price, String originalPrice, String vipPrice, String shopCarNum, String units, String storage, String hasSaled, String imagesPath, String description, String isOnsale, String remark, String status, String createTime, String updateTime, String sort, String detailImgPath, String productBanner, LinkedList<ModelProductParams> productParamsList, String productTitle, String discount, String discountRatio, String shopClassify, String freightage, String customService, String orderNo, String productNum, String flag, String collect) {
        this.id = id;
        this.productNo = productNo;
        this.productsNos = productsNos;
        this.shopNo = shopNo;
        this.shopName = shopName;
        this.menuNo = menuNo;
        this.productCategoryNo = productCategoryNo;
        this.productName = productName;
        this.productType = productType;
        this.price = price;
        this.originalPrice = originalPrice;
        this.vipPrice = vipPrice;
        this.shopCarNum = shopCarNum;
        this.units = units;
        this.storage = storage;
        this.hasSaled = hasSaled;
        this.imagesPath = imagesPath;
        this.description = description;
        this.isOnsale = isOnsale;
        this.remark = remark;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.sort = sort;
        this.detailImgPath = detailImgPath;
        this.productBanner = productBanner;
        this.productParamsList = productParamsList;
//        this.productParams = productParams;
        this.productTitle = productTitle;
        this.discount = discount;
        this.discountRatio = discountRatio;
        this.shopClassify = shopClassify;
        this.freightage = freightage;
        this.customService = customService;
        this.orderNo = orderNo;
        this.productNum = productNum;
        this.flag = flag;
        this.collect = collect;
    }

    public String getIncome() {
        return income == null ? "" : income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getProductParams() {
        return productParams;
    }

    public void setProductParams(String productParams) {
        this.productParams = productParams;
    }

    public String getTotalGrade() {
        return totalGrade;
    }

    public void setTotalGrade(String totalGrade) {
        this.totalGrade = totalGrade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductsNos() {
        return productsNos;
    }

    public void setProductsNos(String productsNos) {
        this.productsNos = productsNos;
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

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

    public String getShopCarNum() {
        return shopCarNum;
    }

    public void setShopCarNum(String shopCarNum) {
        this.shopCarNum = shopCarNum;
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

    public String getIsOnsale() {
        return isOnsale;
    }

    public void setIsOnsale(String isOnsale) {
        this.isOnsale = isOnsale;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDetailImgPath() {
        return detailImgPath;
    }

    public void setDetailImgPath(String detailImgPath) {
        this.detailImgPath = detailImgPath;
    }

    public String getProductBanner() {
        return productBanner;
    }

    public void setProductBanner(String productBanner) {
        this.productBanner = productBanner;
    }

    public LinkedList<ModelProductParams> getProductParamsList() {
        return productParamsList;
    }

    public void setProductParamsList(LinkedList<ModelProductParams> productParamsList) {
        this.productParamsList = productParamsList;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(String discountRatio) {
        this.discountRatio = discountRatio;
    }

    public String getShopClassify() {
        return shopClassify;
    }

    public void setShopClassify(String shopClassify) {
        this.shopClassify = shopClassify;
    }

    public String getFreightage() {
        return freightage;
    }

    public void setFreightage(String freightage) {
        this.freightage = freightage;
    }

    public String getCustomService() {
        return customService;
    }

    public void setCustomService(String customService) {
        this.customService = customService;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }

    public boolean isNotChecked() {
        return unChecked;
    }

    public void setChecked(boolean checked) {
        unChecked = checked;
    }

    public String getProductPlatNo() {
        return productPlatNo == null ? "" : productPlatNo;
    }

    public void setProductPlatNo(String productPlatNo) {
        this.productPlatNo = productPlatNo;
    }

    public int getFeeMode() {
        return feeMode;
    }

    public void setFeeMode(int feeMode) {
        this.feeMode = feeMode;
    }
}
