package com.yimeng.entity;

import java.io.Serializable;

/**
 * 地址信息
 *
 * @author xp
 * @describe 地址信息.
 * @date 2017/10/19.
 */

public class ModelAddress implements Serializable {

    private String linkman;//联系人
    private String mobileNo;//手机号
    private String address;//具体地址
    private String isdefault;//是否默认地址，1为默认，0为非默认
    private String addressNo;//地址编号
    private String province;//省份
    private String city;//城市
    private String area;//区县

    public ModelAddress(String linkman, String mobileNo, String address, String isdefault, String addressNo, String province, String city, String area) {
        this.linkman = linkman;
        this.mobileNo = mobileNo;
        this.address = address;
        this.isdefault = isdefault;
        this.addressNo = addressNo;
        this.province = province;
        this.city = city;
        this.area = area;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(String addressNo) {
        this.addressNo = addressNo;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault;
    }
}
