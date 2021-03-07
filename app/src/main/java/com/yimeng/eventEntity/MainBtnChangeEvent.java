package com.yimeng.eventEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/1/16 0016 上午 10:23.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class MainBtnChangeEvent {
    private int mFragmentType;
    public MainBtnChangeEvent(@FragmentType int type) {
        this.mFragmentType = type;
    }

    public int getChangeFragment() {
        return mFragmentType;
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface FragmentType{
        int home = 1;
        int shop = 2;
        int task = 3;
        int mine = 4;
        int goods_classify = 5;
        int circle = 6;
        int shopCart = 7;
        int order = 8;
        int video = 9;

    }
}
