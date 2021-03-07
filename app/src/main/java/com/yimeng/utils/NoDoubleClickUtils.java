package com.yimeng.utils;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/6 2:17 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 防止按钮连续多次点击
 * </pre>
 */
public class NoDoubleClickUtils {
    private final static int SPACE_TIME = 500;//2次点击的间隔时间，单位ms
    private static long lastClickTime;

    public synchronized static boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        boolean isClick;
        isClick = currentTime - lastClickTime <= SPACE_TIME;
        lastClickTime = currentTime;
        return isClick;
    }
}
