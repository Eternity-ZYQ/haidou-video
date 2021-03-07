package com.yimeng.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/22 4:11 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 流水类型
 * </pre>
 */
@Retention(RetentionPolicy.SOURCE)
public @interface ScoreLogType {
    // 积分
    int score = 1;
    // 活跃度
    int active = 2;
}
