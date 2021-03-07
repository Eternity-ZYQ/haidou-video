package com.yimeng.entity;

import android.text.TextUtils;

/**
 * 店铺信息
 *
 * @author xp
 * @describe 店铺信息.
 * @date 2018/6/5.
 */

public class ModelShop1 {

    private String id;// 店铺编号
    private String image;// LOGO图片
    private String title;// 店铺名称
    private String describe;// 描述
    private String distance;// 距离
    private String grade;// 评分

    private String shopNo;// 店铺编号
    private String shopName;// 店铺名称
    private String imagesPath;// 环境照片和营业执照
    private String address;// 店铺地址
    private String status;// 店铺状态
    private String productCount;// 上架商品数量
    private String shopType;// 店铺类型 service
    private String totalScore;// 总评分
    private String commentNum;// 评论次数
    private String shopScore;//
    private String logoPath;
    private String province;// 省
    private String city;// 城市
    private String area;// 区域
    private int providerNum; // 执行经理人数
    private String logisticsType;// 是否支持送货上门 all / take
    private String repairBrand; //是否缴纳服务费

    private String totalIncome;//累计收益
    private String expectIncome;//预计收益
    private String dayOrderCount;// 今日订单数量
    private String todayOrderCount;// 今日订单数量
    private String dayAmtTotal;// 今日收入
    private String todayIncome;// 今日收入
    private String balance;// 账户余额
    private String totalOrderCount;// 总订单数


    private ModelShopDetail mModelShopDetail;

    public ModelShop1(String id, String image, String title, String describe, String distance, String grade, String shopNo, String shopName, String imagesPath, String address, String status, String dayAmtTotal, String dayOrderCount, String balance, String productCount, String shopScore) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.describe = describe;
        this.distance = distance;
        this.grade = grade;
        this.shopNo = shopNo;
        this.shopName = shopName;
        this.imagesPath = imagesPath;
        this.address = address;
        this.status = status;
        this.dayAmtTotal = dayAmtTotal;
        this.dayOrderCount = dayOrderCount;
        this.balance = balance;
        this.productCount = productCount;
        this.shopScore = shopScore;
    }

    public ModelShop1(String id, String image, String title, String describe, String distance, String grade, String shopScore, String status, int providerNum) {
        this(id, image, title, describe, distance, grade, shopScore);
        this.status = status;
        this.providerNum = providerNum;
    }

    public ModelShop1(String id, String image, String title, String describe, String distance, String grade, String shopScore) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.describe = describe;
        this.distance = distance;
        this.grade = grade;
        this.shopScore = shopScore;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public ModelShopDetail getmModelShopDetail() {
        return mModelShopDetail;
    }

    public void setmModelShopDetail(ModelShopDetail mModelShopDetail) {
        this.mModelShopDetail = mModelShopDetail;
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

    public String getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressStr(){
        return city + area + address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
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
                text = "营业中";
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

    public String getDayAmtTotal() {
        return dayAmtTotal;
    }

    public void setDayAmtTotal(String dayAmtTotal) {
        this.dayAmtTotal = dayAmtTotal;
    }

    public String getDayOrderCount() {
        return dayOrderCount;
    }

    public void setDayOrderCount(String dayOrderCount) {
        this.dayOrderCount = dayOrderCount;
    }

    public String getBalance() {
        return TextUtils.isEmpty(balance) ? "0" : balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getShopScore() {
        return shopScore;
    }

    public void setShopScore(String shopScore) {
        this.shopScore = shopScore;
    }

    /**
     * 执行经理人数
     *
     * @return
     */
    public int getProviderNum() {
        return providerNum;
    }

    public void setProviderNum(int providerNum) {
        this.providerNum = providerNum;
    }

    public String getLogisticsType() {
        return logisticsType;
    }

    public void setLogisticsType(String logisticsType) {
        this.logisticsType = logisticsType;
    }

    public String getLogoPath() {
        return logoPath == null ? "" : logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
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

    public String getRepairBrand() {
        return repairBrand == null ? "" : repairBrand;
    }

    public void setRepairBrand(String repairBrand) {
        this.repairBrand = repairBrand;
    }

    public String getTotalIncome() {
        return totalIncome == null ? "" : totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getExpectIncome() {
        return expectIncome == null ? "" : expectIncome;
    }

    public void setExpectIncome(String expectIncome) {
        this.expectIncome = expectIncome;
    }

    public String getTotalOrderCount() {
        return totalOrderCount == null ? "" : totalOrderCount;
    }

    public void setTotalOrderCount(String totalOrderCount) {
        this.totalOrderCount = totalOrderCount;
    }

    public String getTodayIncome() {
        return todayIncome == null ? "" : todayIncome;
    }

    public void setTodayIncome(String todayIncome) {
        this.todayIncome = todayIncome;
    }

    public String getTodayOrderCount() {
        return todayOrderCount == null ? "" : todayOrderCount;
    }

    public void setTodayOrderCount(String todayOrderCount) {
        this.todayOrderCount = todayOrderCount;
    }
}
