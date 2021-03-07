package com.yimeng.entity;

import java.io.Serializable;

/**
 * Created by xp on 2016/1/11.
 * 简单的model
 */
public class ModelSimple implements Serializable {

    private String id;           // id编号
    private String text;         // 文字
    private String value;        // 对应值
    private int position;        // 位置
    private int fPosition;       // 上级位置
    private boolean checked;     // true选中，false未选中
    private double lat;
    private double lng;
    private int type;

    public ModelSimple() {
    }

    public ModelSimple(String text) {
        this.text = text;
    }

    public ModelSimple(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public ModelSimple(String id, String text, String value) {
        this.id = id;
        this.text = text;
        this.value = value;
    }

    public ModelSimple(String id, String text, String value, int type) {
        this.id = id;
        this.text = text;
        this.value = value;
        this.type = type;
    }

    public ModelSimple(String id, String text, String value, int position, int fPosition, boolean checked) {
        this.id = id;
        this.text = text;
        this.value = value;
        this.position = position;
        this.fPosition = fPosition;
        this.checked = checked;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getfPosition() {
        return fPosition;
    }

    public void setfPosition(int fPosition) {
        this.fPosition = fPosition;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
