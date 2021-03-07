package com.yimeng.interfaces;

import com.yimeng.entity.Member;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/9 11:23 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 获取用户信息
 * </pre>
 */
public interface OnGetMemberInfoCallBack {
    void getMemberInfo(Member member);
}
