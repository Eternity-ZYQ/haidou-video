package com.yimeng.entity;

import java.io.Serializable;

/**
 * 交易记录
 *
 * @author xp
 * @describe 交易记录.
 * @date 2018/6/7.
 */
public class ModelTransactionRecords implements Serializable {
    private String id;
    private String title;
    private String describe;
    private String other;// 如+3,-1
    private String scoreInOut;//in代表收入，显示金额时显示如“+1000”，out代表支出，显示金额时显示如“-1000”

    public ModelTransactionRecords(String id, String title, String describe, String other) {
        this.id = id;
        this.title = title;
        this.describe = describe;
        this.other = other;
    }

    public String getScoreInOut() {
        return scoreInOut;
    }

    public void setScoreInOut(String scoreInOut) {
        this.scoreInOut = scoreInOut;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
