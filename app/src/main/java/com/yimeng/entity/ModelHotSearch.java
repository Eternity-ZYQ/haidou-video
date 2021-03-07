package com.yimeng.entity;

/**
 * Created by user on 2018/6/28.
 * 热门搜索 同 搜索结果分页列表(少于20条)
 * 热门推荐
 */

public class ModelHotSearch {

    private String id;
    private String searchNo;
    private String name;
    private String count;
    private String isShow;
    private String showSort;
    private String location;
    private String status;
    private String remark;
    private String createTime;
    private String updateTime;

    public ModelHotSearch(String id, String searchNo, String name, String count, String isShow, String showSort, String location, String status, String remark, String createTime, String updateTime) {
        this.id = id;
        this.searchNo = searchNo;
        this.name = name;
        this.count = count;
        this.isShow = isShow;
        this.showSort = showSort;
        this.location = location;
        this.status = status;
        this.remark = remark;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearchNo() {
        return searchNo;
    }

    public void setSearchNo(String searchNo) {
        this.searchNo = searchNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getShowSort() {
        return showSort;
    }

    public void setShowSort(String showSort) {
        this.showSort = showSort;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}


