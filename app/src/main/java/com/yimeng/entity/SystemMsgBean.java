package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/21 3:13 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class SystemMsgBean {

    /**
     * name : 系统提示
     * introduction : 链接地址
     * sort : 0
     * logo : /fileupload/shopImage/20190621/15611005685901321468842(365x478).jpg
     * createTime : 1561100575000
     * updateTime : 1561100609000
     */

    private String name;
    private String introduction;
    private int sort;   // 为1跳转网页，为0不跳转
    private String logo;
    private String parentNo;
    private long createTime;
    private long updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public String getParentNo() {
        return parentNo == null ? "" : parentNo;
    }

    public void setParentNo(String parentNo) {
        this.parentNo = parentNo;
    }
}
