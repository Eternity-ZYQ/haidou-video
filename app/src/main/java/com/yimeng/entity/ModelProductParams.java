package com.yimeng.entity;

import java.io.Serializable;

/**
 * Created by user on 2018/7/4.
 */

public class ModelProductParams implements Serializable {

    String key;
    String value;
    String sort;

    public ModelProductParams(String key, String value, String sort) {
        this.key = key;
        this.value = value;
        this.sort = sort;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
