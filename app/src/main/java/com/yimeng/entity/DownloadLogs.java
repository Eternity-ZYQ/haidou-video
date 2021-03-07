package com.yimeng.entity;

import org.litepal.crud.LitePalSupport;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/4 5:18 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 下载日志实体
 * </pre>
 */
public class DownloadLogs extends LitePalSupport {

    private String memberNo;
    private String taskNo;
    private long downloadTime;

    public DownloadLogs(String memberNo, String taskNo, long downloadTime) {
        this.memberNo = memberNo;
        this.taskNo = taskNo;
        this.downloadTime = downloadTime;
    }

    public String getMemberNo() {
        return memberNo == null ? "" : memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getTaskNo() {
        return taskNo == null ? "" : taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public long getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
    }
}
