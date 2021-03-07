package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/4/17 2:22 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 代理结算积分积分记录
 * </pre>
 */
public class ProxyMemberScoreBean {
    private String category; //积分分类
    private String scoreType;//积分类型
    private String scoreInOut;//积分进出
    private String logName;//记录名称
    private String score;//积分值
    private long createTime;//创建时间

    public String getCategory() {
        return category == null ? "" : category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getScoreType() {
        return scoreType == null ? "" : scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public String getScoreInOut() {
        return scoreInOut == null ? "" : scoreInOut;
    }

    public void setScoreInOut(String scoreInOut) {
        this.scoreInOut = scoreInOut;
    }

    public String getLogName() {
        return logName == null ? "" : logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getScore() {
        return score == null ? "" : score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
