package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/12/20 0020 下午 05:30.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class CashLogsBean {


    private int id;
    private String scoreType;
    private String scoreInOut;
    private String logName;
    private String score;
    private long createTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public String getScoreInOut() {
        return scoreInOut;
    }

    public void setScoreInOut(String scoreInOut) {
        this.scoreInOut = scoreInOut;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getScore() {
        return score;
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
