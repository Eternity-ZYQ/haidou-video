package com.yimeng.utils;

import android.view.View;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/14 10:32 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class ViewUtils {

    /**
     * 判断x，y是否在view范围内
     */
    public static boolean isTouchPointInView(View view, int x, int y){
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        return y >= top && y <= bottom && x >= left
                && x <= right;
    }
}
