package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/23 0023 下午 06:37.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 店铺详情分类
 * </pre>
 */
public class ShopDetailClassifyBean {

    /**
     * productCategoryName : 美颜1
     * productCount : 1
     * productCategoryNo : 201809231541365451545
     */

    private String productCategoryName;
    private int productCount;
    private String productCategoryNo;
    private String logo; //  图片
    /**
     * 是否选中
     */
    private boolean checked;

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public String getProductCategoryNo() {
        return productCategoryNo;
    }

    public void setProductCategoryNo(String productCategoryNo) {
        this.productCategoryNo = productCategoryNo;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getLogo() {
        return logo == null ? "" : logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
