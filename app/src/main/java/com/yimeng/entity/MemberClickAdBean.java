package com.yimeng.entity;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-18 01:13.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class MemberClickAdBean {

    // 用户id
    private String memberNo;
    // 最后点击广告时间
    private long lastClickTime;
    // 完成次数
    private int count;

    public String getMemberNo() {
        return memberNo == null ? "" : memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public long getLastClickTime() {
        return lastClickTime;
    }

    public void setLastClickTime(long lastClickTime) {
        this.lastClickTime = lastClickTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
