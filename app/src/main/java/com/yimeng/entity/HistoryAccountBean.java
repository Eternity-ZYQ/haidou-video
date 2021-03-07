package com.yimeng.entity;

import org.litepal.crud.LitePalSupport;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/8 2:36 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 历史账号
 * </pre>
 */
public class HistoryAccountBean extends LitePalSupport {

    private String headpath;
    private String accountNo;
    private String pwd;

    public HistoryAccountBean(String accountNo, String pwd, String headpath) {
        this.accountNo = accountNo;
        this.pwd = pwd;
        this.headpath = headpath;
    }

    public String getAccountNo() {
        return accountNo == null ? "" : accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getPwd() {
        return pwd == null ? "" : pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getHeadpath() {
        return headpath == null ? "" : headpath;
    }

    public void setHeadpath(String headpath) {
        this.headpath = headpath;
    }
}
