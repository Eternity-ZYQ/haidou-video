package com.yimeng.entity;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/26 10:02 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class RedPackerStyleBean implements IPickerViewData {


    /**
     * id : 9
     * modeNo : 201908231542536456524
     * modeType : qita
     * modeCategory :
     * modeName : 12312
     * modeImg : /fileupload/shopImage/20190823/1566546181311647905042(433x157).jpg
     * modeContent : 123121231少时诵诗书所多撒奥所多打萨达十大所多撒大所大所大
     * modePreviewLink : null
     * modeCreateLink : null
     * modeEditLink : null
     * modeDynamicLink : null
     * status : common
     * remark :
     * createTime : 1566546184000
     * updateTime : 1566546495000
     */

    private String modeNo;
    private String modeType;
    private String modeCategory;
    private String modeName;
    private String modeImg;
    private String modeContent;
    private Object modePreviewLink;
    private Object modeCreateLink;
    private Object modeEditLink;
    private Object modeDynamicLink;
    private String status;
    private String remark;

    public String getModeNo() {
        return modeNo == null ? "" : modeNo;
    }

    public void setModeNo(String modeNo) {
        this.modeNo = modeNo;
    }

    public String getModeType() {
        return modeType == null ? "" : modeType;
    }

    public void setModeType(String modeType) {
        this.modeType = modeType;
    }

    public String getModeCategory() {
        return modeCategory == null ? "" : modeCategory;
    }

    public void setModeCategory(String modeCategory) {
        this.modeCategory = modeCategory;
    }

    public String getModeName() {
        return modeName == null ? "" : modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getModeImg() {
        return modeImg == null ? "" : modeImg;
    }

    public void setModeImg(String modeImg) {
        this.modeImg = modeImg;
    }

    public String getModeContent() {
        return modeContent == null ? "" : modeContent;
    }

    public void setModeContent(String modeContent) {
        this.modeContent = modeContent;
    }

    public Object getModePreviewLink() {
        return modePreviewLink;
    }

    public void setModePreviewLink(Object modePreviewLink) {
        this.modePreviewLink = modePreviewLink;
    }

    public Object getModeCreateLink() {
        return modeCreateLink;
    }

    public void setModeCreateLink(Object modeCreateLink) {
        this.modeCreateLink = modeCreateLink;
    }

    public Object getModeEditLink() {
        return modeEditLink;
    }

    public void setModeEditLink(Object modeEditLink) {
        this.modeEditLink = modeEditLink;
    }

    public Object getModeDynamicLink() {
        return modeDynamicLink;
    }

    public void setModeDynamicLink(Object modeDynamicLink) {
        this.modeDynamicLink = modeDynamicLink;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String getPickerViewText() {
        return this.modeName;
    }
}
