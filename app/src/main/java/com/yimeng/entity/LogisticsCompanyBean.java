package com.yimeng.entity;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/3/27 0027 下午 03:20.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 物流公司
 * </pre>
 */
public class LogisticsCompanyBean implements IPickerViewData {

    /**
     * id : 1
     * logisticsCompanyNo : 1537941721
     * logisticsCompanyName : EMS快递
     * logisticsCompanyPinyin : ems
     * status : common
     * remark : null
     * createTime : 1537941520000
     * updateTime : 1537941520000
     */

    private int id;
    private String logisticsCompanyNo;
    private String logisticsCompanyName;
    private String logisticsCompanyPinyin;
    private String status;
    private Object remark;
    private long createTime;
    private long updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogisticsCompanyNo() {
        return logisticsCompanyNo;
    }

    public void setLogisticsCompanyNo(String logisticsCompanyNo) {
        this.logisticsCompanyNo = logisticsCompanyNo;
    }

    public String getLogisticsCompanyName() {
        return logisticsCompanyName;
    }

    public void setLogisticsCompanyName(String logisticsCompanyName) {
        this.logisticsCompanyName = logisticsCompanyName;
    }

    public String getLogisticsCompanyPinyin() {
        return logisticsCompanyPinyin;
    }

    public void setLogisticsCompanyPinyin(String logisticsCompanyPinyin) {
        this.logisticsCompanyPinyin = logisticsCompanyPinyin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
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

    @Override
    public String getPickerViewText() {
        return logisticsCompanyName;
    }
}
