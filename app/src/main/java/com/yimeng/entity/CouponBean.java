package com.yimeng.entity;

import java.io.Serializable;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/3/27 0027 下午 04:51.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 代金券
 * </pre>
 */
public class CouponBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 代金券编号
     */
    private String couponNo;

    /**
     * 代金券类型
     */
    private String couponType;

    /**
     * 代金券商家编号
     */
    private String couponShopNo;

    /**
     * 会员编号
     */
    private String memberNo;

    /**
     * 代金券金额
     */
    private Long amt;

    /**
     * 是否使用
     */
    private Integer used;

    /**
     * 使用时间
     */
    private long usedTime;

    /**
     * 使用订单号
     */
    private String usedOrder;

    /**
     * 是否可转让 0 不可以， 1可以
     */
    private Integer transfered;

    /**
     * 来源
     */
    private String couponSrc;

    /**
     * 来源编号
     */
    private String couponSrcNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private String status;

    /**
     * 过期时间
     */
    private long expiredTime;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 更新时间
     */
    private long updateTime;

    /**
     * 获取id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取代金券编号
     */
    public String getCouponNo() {
        return couponNo;
    }

    /**
     * 设置代金券编号
     */
    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    /**
     * 获取代金券类型
     */
    public String getCouponType() {
        return couponType;
    }

    /**
     * 设置代金券类型
     */
    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    /**
     * 获取代金券商家编号
     */
    public String getCouponShopNo() {
        return couponShopNo;
    }

    /**
     * 设置代金券商家编号
     */
    public void setCouponShopNo(String couponShopNo) {
        this.couponShopNo = couponShopNo;
    }

    /**
     * 获取会员编号
     */
    public String getMemberNo() {
        return memberNo;
    }

    /**
     * 设置会员编号
     */
    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    /**
     * 获取是否使用
     */
    public Integer getUsed() {
        return used;
    }

    /**
     * 设置是否使用
     */
    public void setUsed(Integer used) {
        this.used = used;
    }

    /**
     * 获取使用时间
     */
    public long getUsedTime() {
        return usedTime;
    }

    /**
     * 设置使用时间
     */
    public void setUsedTime(long usedTime) {
        this.usedTime = usedTime;
    }

    /**
     * 获取备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取创建时间
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     */
    public long getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     */
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUsedOrder() {
        return usedOrder;
    }

    public void setUsedOrder(String usedOrder) {
        this.usedOrder = usedOrder;
    }

    public Integer getTransfered() {
        return transfered;
    }

    public void setTransfered(Integer transfered) {
        this.transfered = transfered;
    }

    public String getCouponSrc() {
        return couponSrc;
    }

    public void setCouponSrc(String couponSrc) {
        this.couponSrc = couponSrc;
    }

    public String getCouponSrcNo() {
        return couponSrcNo;
    }

    public void setCouponSrcNo(String couponSrcNo) {
        this.couponSrcNo = couponSrcNo;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Long getAmt() {
        return amt;
    }

    public void setAmt(Long amt) {
        this.amt = amt;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Coupon [id=");
        builder.append(id);
        builder.append(", couponNo=");
        builder.append(couponNo);
        builder.append(", couponType=");
        builder.append(couponType);
        builder.append(", couponShopNo=");
        builder.append(couponShopNo);
        builder.append(", memberNo=");
        builder.append(memberNo);
        builder.append(", used=");
        builder.append(used);
        builder.append(", usedTime=");
        builder.append(usedTime);
        builder.append(", remark=");
        builder.append(remark);
        builder.append(", status=");
        builder.append(status);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append(", updateTime=");
        builder.append(updateTime);
        builder.append("]");
        return builder.toString();
    }
}
