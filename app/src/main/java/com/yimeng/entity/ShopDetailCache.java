package com.yimeng.entity;

import org.litepal.crud.LitePalSupport;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/29 6:28 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 申请店铺输入缓存
 * </pre>
 */
public class ShopDetailCache extends LitePalSupport {

    private String shopName;// 店铺名称
    private String mobileNo;// 联系人
    private String telephone;// 联系人电话
    private String province;// 省
    private String city;// 城市
    private String area;// 区域
    private String address;// 地址
    private String introduce;// 店铺简介

    public String getShopName() {
        return shopName == null ? "" : shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getMobileNo() {
        return mobileNo == null ? "" : mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getTelephone() {
        return telephone == null ? "" : telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getProvince() {
        return province == null ? "" : province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city == null ? "" : city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area == null ? "" : area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduce() {
        return introduce == null ? "" : introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
