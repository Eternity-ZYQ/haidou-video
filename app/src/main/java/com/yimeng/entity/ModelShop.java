package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/1/4 0004 下午 08:14.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class ModelShop {

    /**
     * shopNo : 201811091520571027692
     * shopType : sale
     * shopName : 龙华小铺
     * telephone : 12055550001
     * mobileNo : 龙华
     * logoPath : /fileupload/shopImage/20181118/1542507946413.png
     * imagesPath : /fileupload/shopImage/20181118/1542507933410.png,/fileupload/shopImage/20181118/1542507957332.png,/fileupload/shopImage/20181109/1541748054436.png
     * distance : 1305
     * longitude : 114.033835
     * latitude : 22.678469
     * province : null
     * city : 深圳市
     * area : 龙华区
     * address : 龙华区华韵路93号
     * keyWords : null
     * totalScore : 18
     * commentNum : 5
     * totalSale : 0
     * introduce : 龙华新区店
     * description : null
     * openTime : 07:00:00
     * openTimeStr : 07:00
     * closeTime : 22:00:00
     * closeTimeStr : 22:00
     * isCollected : null
     * identityPath : null
     * shopScore : 3
     * logisticsType : all
     */

    private String shopNo;
    private String shopType;
    private String shopName;
    private String telephone;
    private String mobileNo;
    private String logoPath;
    private String imagesPath;
    private int distance;
    private String longitude;
    private String latitude;
    private Object province;
    private String city;
    private String area;
    private String address;
    private Object keyWords;
    private int totalScore;
    private int commentNum;
    private int totalSale;
    private String introduce;
    private Object description;
    private String openTime;
    private String openTimeStr;
    private String closeTime;
    private String closeTimeStr;
    private Object isCollected;
    private Object identityPath;
    private int shopScore;
    private String logisticsType;
    private String status;

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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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

    public Object getProvince() {
        return province;
    }

    public void setProvince(Object province) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(Object keyWords) {
        this.keyWords = keyWords;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(int totalSale) {
        this.totalSale = totalSale;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getOpenTimeStr() {
        return openTimeStr;
    }

    public void setOpenTimeStr(String openTimeStr) {
        this.openTimeStr = openTimeStr;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getCloseTimeStr() {
        return closeTimeStr;
    }

    public void setCloseTimeStr(String closeTimeStr) {
        this.closeTimeStr = closeTimeStr;
    }

    public Object getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(Object isCollected) {
        this.isCollected = isCollected;
    }

    public Object getIdentityPath() {
        return identityPath;
    }

    public void setIdentityPath(Object identityPath) {
        this.identityPath = identityPath;
    }

    public int getShopScore() {
        return shopScore;
    }

    public void setShopScore(int shopScore) {
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
}
