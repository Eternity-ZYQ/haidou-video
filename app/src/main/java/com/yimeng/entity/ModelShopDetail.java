package com.yimeng.entity;

import java.io.Serializable;

/**
 * 店铺详情
 *
 * @author xp
 * @describe 店铺详情.
 * @date 2018/6/5.
 */

public class ModelShopDetail implements Serializable {

    private String shopNo;// 店铺编号
    private String shopType;// 店铺类型
    private String shopName;// 店铺名称
    private String telephone;// 联系人电话
    private String mobileNo;// 联系人
    private String logoPath;// logo图标地址
    private String imagesPath;// 多张图片地址
    private String longitude;// 纬度
    private String latitude;// 经度
    private String distance;// 距离
    private String address;// 地址
    private String totalScore;// 评分
    private String introduce;// 店铺简介
    private String province;// 省
    private String city;// 城市
    private String area;// 区域
    private boolean isCollected;// 是否收藏
    private String identityPath;// 身份信息照片,隔开
    private String openTimeStr;// 营业开始时间
    private String closeTimeStr;// 营业结束时间
    private String remark;// 备注
    private String shopScore;
    private String logisticsType;// 是否支持配送
    private String status; // 店铺状态;
    private String merchantNo; //店主id
    private String repairTypeNo; // 维修类别
    private String repairBrand;// 擅长维修品牌描述
    private String manufacturersAuth;// 厂家授权照片
    private String repairDevice;// 维修设备照片


    public ModelShopDetail(String shopNo, String shopType, String shopName, String telephone, String mobileNo, String logoPath, String imagesPath, String longitude, String latitude, String distance, String address, String totalScore, String introduce, String province, String city, String area, boolean isCollected, String identityPath, String openTimeStr, String closeTimeStr, String shopScore) {
        this.shopNo = shopNo;
        this.shopType = shopType;
        this.shopName = shopName;
        this.telephone = telephone;
        this.mobileNo = mobileNo;
        this.logoPath = logoPath;
        this.imagesPath = imagesPath;
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = distance;
        this.address = address;
        this.totalScore = totalScore;
        this.introduce = introduce;
        this.province = province;
        this.city = city;
        this.area = area;
        this.isCollected = isCollected;
        this.identityPath = identityPath;
        this.openTimeStr = openTimeStr;
        this.closeTimeStr = closeTimeStr;
        this.shopScore = shopScore;
    }

    public ModelShopDetail(String shopNo, String shopType, String shopName, String telephone,
                           String mobileNo, String logoPath, String imagesPath, String longitude, String latitude,
                           String distance, String address, String totalScore, String introduce, String province,
                           String city, String area, boolean isCollected, String identityPath, String openTimeStr,
                           String closeTimeStr, String shopScore, String status, String merchantNo){

        this(shopNo, shopType, shopName, telephone, mobileNo, logoPath, imagesPath, longitude, latitude, distance,
                address, totalScore, introduce, province, city, area, isCollected, identityPath, openTimeStr,
                closeTimeStr, shopScore);
        this.status = status;
        this.merchantNo = merchantNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOpenTimeStr() {
        return openTimeStr;
    }

    public void setOpenTimeStr(String openTimeStr) {
        this.openTimeStr = openTimeStr;
    }

    public String getCloseTimeStr() {
        return closeTimeStr;
    }

    public void setCloseTimeStr(String closeTimeStr) {
        this.closeTimeStr = closeTimeStr;
    }

    public String getIdentityPath() {
        return identityPath;
    }

    public void setIdentityPath(String identityPath) {
        this.identityPath = identityPath;
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

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    /**
     * @return  快递物流店
     */
    public boolean isShopExpress(){
        return shopType.equals("express") || shopType.equals("logistics");
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

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getShopScore() {
        return shopScore;
    }

    public void setShopScore(String shopScore) {
        this.shopScore = shopScore;
    }

    public String getLogisticsType() {
        return logisticsType;
    }

    public void setLogisticsType(String logisticsType) {
        this.logisticsType = logisticsType;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        String text = "";
        switch (status) {
            case "apply":
                text = "申请中";
                break;
            case "common":
                text = "正常";
                break;
            case "nopass":
                text = "未通过";
                break;
            case "reapply":
                text = "重审";
                break;
            case "freeze":
                text = "冻结";
                break;
            case "blacklist":
                text = "黑名单";
                break;
            case "rest":
                text = "休息中";
                break;
            case "closed":
                text = "关闭";
                break;
            case "applyfail":
                text = "申请失败";
                break;
            case "reapplyfail":
                text = "重审失败";
                break;
            case "delete":
                text = "删除";
                break;
            default:
                break;
        }
        return text;
    }

    public String getMerchantNo() {
        return merchantNo == null ? "" : merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getRepairTypeNo() {
        return repairTypeNo == null ? "" : repairTypeNo;
    }

    public void setRepairTypeNo(String repairTypeNo) {
        this.repairTypeNo = repairTypeNo;
    }

    public String getRepairBrand() {
        return repairBrand == null ? "" : repairBrand;
    }

    public void setRepairBrand(String repairBrand) {
        this.repairBrand = repairBrand;
    }

    public String getManufacturersAuth() {
        return manufacturersAuth == null ? "" : manufacturersAuth;
    }

    public void setManufacturersAuth(String manufacturersAuth) {
        this.manufacturersAuth = manufacturersAuth;
    }

    public String getRepairDevice() {
        return repairDevice == null ? "" : repairDevice;
    }

    public void setRepairDevice(String repairDevice) {
        this.repairDevice = repairDevice;
    }

    public String getAddressStr() {
        return city + area + address;
    }
}
