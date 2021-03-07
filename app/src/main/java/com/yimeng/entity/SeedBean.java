package com.yimeng.entity;

import org.litepal.crud.LitePalSupport;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/5 3:21 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 发财种子
 * </pre>
 */
public class SeedBean extends LitePalSupport {

    /**
     * id : 14359
     * mtSeedNo : SD201908160001001206352
     * seedType : system
     * seedImage : null
     * memberNo : null
     * used : 0
     * usedTime : null
     * transfered : 1
     * seedSrc : platform
     * seedSrcNo : cuxiaoPlatformFund
     * amt : 0
     * expiredTime : 1566144060000
     * downTime : 1565886409000
     * remark : null
     * status : initial
     * createTime : 1565884860000
     * updateTime : 1565884860000
     * nickname : null
     */

    private String mtSeedNo;
    private String seedType;
    private int used;
    private long usedTime;
    private int transfered;
    private String seedSrc;
    private String seedSrcNo;
    private int amt;
    private long expiredTime;
    private long downTime;
    private String status;
    private long createTime;
    private long updateTime;

    public String getMtSeedNo() {
        return mtSeedNo == null ? "" : mtSeedNo;
    }

    public void setMtSeedNo(String mtSeedNo) {
        this.mtSeedNo = mtSeedNo;
    }

    public String getSeedType() {
        return seedType == null ? "" : seedType;
    }

    public void setSeedType(String seedType) {
        this.seedType = seedType;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public long getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(long usedTime) {
        this.usedTime = usedTime;
    }

    public int getTransfered() {
        return transfered;
    }

    public void setTransfered(int transfered) {
        this.transfered = transfered;
    }

    public String getSeedSrc() {
        return seedSrc == null ? "" : seedSrc;
    }

    public void setSeedSrc(String seedSrc) {
        this.seedSrc = seedSrc;
    }

    public String getSeedSrcNo() {
        return seedSrcNo == null ? "" : seedSrcNo;
    }

    public void setSeedSrcNo(String seedSrcNo) {
        this.seedSrcNo = seedSrcNo;
    }

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public long getDownTime() {
        return downTime;
    }

    public void setDownTime(long downTime) {
        this.downTime = downTime;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
