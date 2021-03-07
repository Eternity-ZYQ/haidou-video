package com.yimeng.entity;


import java.io.Serializable;

/**
 * 商品信息
 */
public class GoodsModel implements Serializable {

    private String productNo;// 商品编号
    private String productName;// 商品名称
    private String imagesPath;// 商品图片地址，多个,可以隔开
    private String description;// 商品描述
    private String price;// 商品价格
    private String units;// 商品单位
    private String productCategoryNo;// 商家分类编号
    private String shopNo;// shopNo
    private boolean collect;// true(收藏)，false(未收藏)
    private boolean check;// true(选中)，false(未选中)
    private String productOrderStatus; // 订单商品状态
    private String productColorSize; // 商品型号颜色
    private String menuNo; // 商品类型编号
    private String orderProductNo; // 订单商品编号
    private String productNum; // 数量
    private String inventory;//库存
    private String style;//款式
    private String specification;//规格
    private String costBeans; //花费麻豆
    private String remark;// 驳回信息
    private int hasSaled;// 已销售数量
    private int storage; //库存
    private String isOnsale;// 是否上架   1: 上架 0: 下架

    private int position;// 位置
    private int fPosition;// 位置
    private int count;// 商品数量
    private String originalPrice; // 进货价

    private String productPlatNo; // 平台商品编号

    private String productType;// 产品类型 entity实体店,virtual商城
    private int feeMode; //商品服务类型   0。线上（仅支持送货）1。到店（仅支持到店消费）2。都支持
    private String vipPrice; //供货价

    public GoodsModel() {
    }

    public GoodsModel(String orderProductNo, String productOrderStatus, String menuNo, String productNo, String productName, String productColorSize, String units, String imagesPath, String productNum, String price) {
        this.orderProductNo = orderProductNo;
        this.productOrderStatus = productOrderStatus;
        this.menuNo = menuNo;
        this.productNo = productNo;
        this.productName = productName;
        this.productColorSize = productColorSize;
        this.units = units;
        this.imagesPath = imagesPath;
        this.productNum = productNum;
        this.price = price;
    }

    public GoodsModel(String productNo, String productName, String imagesPath, String description, String price, String units, String productCategoryNo, String shopNo, boolean collect) {
        this.productNo = productNo;
        this.productName = productName;
        this.imagesPath = imagesPath;
        this.description = description;
        this.price = price;
        this.units = units;
        this.productCategoryNo = productCategoryNo;
        this.shopNo = shopNo;
        this.collect = collect;
    }

    public GoodsModel(String productName, String imagesPath, String price, String style, String specification, String inventory, String productNum, String costBeans) {
        this.productName = productName;
        this.imagesPath = imagesPath;
        this.price = price;
        this.style = style;
        this.specification = specification;
        this.inventory = inventory;
        this.productNum = productNum;
        this.costBeans = costBeans;
    }

    public String getIsOnsale() {
        return isOnsale == null ? "" : isOnsale;
    }

    public void setIsOnsale(String isOnsale) {
        this.isOnsale = isOnsale;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCostBeans() {
        return costBeans;
    }

    public void setCostBeans(String costBeans) {
        this.costBeans = costBeans;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getProductOrderStatus() {
        return productOrderStatus;
    }

    public void setProductOrderStatus(String productOrderStatus) {
        this.productOrderStatus = productOrderStatus;
    }

    public String getProductColorSize() {
        return productColorSize;
    }

    public void setProductColorSize(String productColorSize) {
        this.productColorSize = productColorSize;
    }

    public String getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(String menuNo) {
        this.menuNo = menuNo;
    }

    public String getOrderProductNo() {
        return orderProductNo;
    }

    public void setOrderProductNo(String orderProductNo) {
        this.orderProductNo = orderProductNo;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getfPosition() {
        return fPosition;
    }

    public void setfPosition(int fPosition) {
        this.fPosition = fPosition;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public String getImagesPathLogo() {
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

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getHasSaled() {
        return hasSaled;
    }

    public void setHasSaled(int hasSaled) {
        this.hasSaled = hasSaled;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public String getProductPlatNo() {
        return productPlatNo == null ? "" : productPlatNo;
    }

    public void setProductPlatNo(String productPlatNo) {
        this.productPlatNo = productPlatNo;
    }

    public String getOriginalPrice() {
        return originalPrice == null ? "" : originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getFeeMode() {
        return feeMode;
    }

    public void setFeeMode(int feeMode) {
        this.feeMode = feeMode;
    }

    public String getVipPrice() {
        return vipPrice == null ? "" : vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }
}
